import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { JwtPayload, LoginRequest } from '../types';
import { authService } from '../services/api';

interface AuthContextType {
    user: JwtPayload | null;
    isAuthenticated: boolean;
    isLoading: boolean;
    login: (dados: LoginRequest) => Promise<void>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Função simples para decodificar JWT sem libs externas
function parseJwt(token: string): JwtPayload | null {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(jsonPayload);
    } catch (e) {
        return null;
    }
}

export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<JwtPayload | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            const decoded = parseJwt(token);
            if (decoded && decoded.exp * 1000 > Date.now()) {
                setUser(decoded);
            } else {
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
            }
        }
        setIsLoading(false);
    }, []);

    const login = async (dados: LoginRequest) => {
        const response = await authService.login(dados);
        const { accessToken, refreshToken } = response.data;
        
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);
        
        setUser(parseJwt(accessToken));
    };

    const logout = () => {
        authService.logout();
        setUser(null);
        window.location.href = '/login';
    };

    const value = {
        user,
        isAuthenticated: !!user,
        isLoading,
        login,
        logout
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth deve ser usado dentro de um AuthProvider');
    }
    return context;
}
