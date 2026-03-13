// web/src/services/api.ts
import axios from 'axios';
import type {
    ImovelResponse,
    ImovelRequest,
    ContratoResponse,
    ContratoRequest,
    StatusContrato,
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