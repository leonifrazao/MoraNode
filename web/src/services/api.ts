// web/src/services/api.ts
import axios from 'axios';
import type {
    ImovelResponse,
    ImovelRequest,
    ContratoResponse,
    ContratoRequest,
    StatusContrato,
    LoginRequest,
    RegistroRequest,
    TokenResponse
} from '../types';

// Em produção (Docker), usa /api/ que o Nginx faz proxy
// Em desenvolvimento, usa localhost:8080 diretamente
const baseURL = import.meta.env.PROD
    ? '/api'
    : 'http://localhost:8080';

const api = axios.create({
    baseURL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Interceptor de Requisição para injetar o Token no Header
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('accessToken');
        if (token && config.headers) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Interceptor de Resposta para tentar Renovação se der 401
api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        // Evita interceptar e recarregar a página se a requisição original for a de login
        if (originalRequest.url?.includes('/auth/login')) {
            return Promise.reject(error);
        }

        // Se o erro foi 401 (Não autorizado) e ainda não tentamos renovar (evitar loop infinito)
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            const refreshToken = localStorage.getItem('refreshToken');
            
            if (refreshToken) {
                try {
                    // Tenta falar de modo paralelo pro Backend para trocar o Refresh Token
                    const response = await axios.post<TokenResponse>(`${baseURL}/auth/refresh`, {
                        refreshToken,
                    });

                    const { accessToken, refreshToken: newRefreshToken } = response.data;

                    // Salva os novos tokens
                    localStorage.setItem('accessToken', accessToken);
                    localStorage.setItem('refreshToken', newRefreshToken);

                    // Refaz a request original que falhou só que agora com o token limpo
                    if (originalRequest.headers) {
                        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
                    }
                    return api(originalRequest);
                } catch (refreshError) {
                    // Se o Refresh também falhou ou expirou, manda pro login matando a sessão
                    localStorage.removeItem('accessToken');
                    localStorage.removeItem('refreshToken');
                    window.location.href = '/login';
                    return Promise.reject(refreshError);
                }
            } else {
                localStorage.removeItem('accessToken');
                window.location.href = '/login';
            }
        }
        return Promise.reject(error);
    }
);

export const authService = {
    login: (dados: LoginRequest) => api.post<TokenResponse>('/auth/login', dados),
    registro: (dados: RegistroRequest) => api.post<void>('/auth/registro', dados),
    // Logout só limpa no front já que é JWT stateless (não tem blacklist ainda)
    logout: () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
    }
}

export const imovelService = {
    listar: () => api.get<ImovelResponse[]>('/imoveis'),
    buscarPorId: (id: number) => api.get<ImovelResponse>(`/imoveis/${id}`),
    cadastrar: (dados: ImovelRequest) => api.post<void>('/imoveis', dados),
    editar: (id: number, dados: ImovelRequest) => api.put<void>(`/imoveis/${id}`, dados),
    deletar: (id: number) => api.delete<void>(`/imoveis/${id}`),
};

export const contratoService = {
    listar: () => api.get<ContratoResponse[]>('/contratos'),
    buscarPorId: (id: number) => api.get<ContratoResponse>(`/contratos/${id}`),
    cadastrar: (dados: ContratoRequest) => api.post<void>('/contratos', dados),
    atualizarStatus: (id: number, status: StatusContrato) =>
        api.patch<void>(`/contratos/${id}/status`, { statusContrato: status }),
};

export default api;