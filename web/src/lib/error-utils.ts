// src/lib/error-utils.ts
import { AxiosError } from "axios";
import { ErrorResponse } from "@/types";

/**
 * Extrai mensagem de erro amigável de uma resposta do backend
 */
export function getErrorMessage(error: unknown): string {
    // Se for erro do Axios com resposta do servidor
    if (isAxiosError(error) && error.response?.data) {
        const data = error.response.data as ErrorResponse;

        // Se tem a estrutura do ErrorResponse do Java
        if (data.message) {
            return data.message;
        }

        // Se for string direta
        if (typeof error.response.data === "string") {
            return error.response.data;
        }
    }

    // Se for erro do Axios sem resposta (rede, timeout, etc)
    if (isAxiosError(error) && !error.response) {
        if (error.code === "ERR_NETWORK") {
            return "Erro de conexão. Verifique se o servidor está rodando.";
        }
        if (error.code === "ECONNABORTED") {
            return "Tempo de conexão esgotado. Tente novamente.";
        }
        return "Erro de rede. Verifique sua conexão.";
    }

    // Se for Error padrão
    if (error instanceof Error) {
        return error.message;
    }

    // Fallback
    return "Ocorreu um erro inesperado.";
}

/**
 * Extrai título do erro baseado no status HTTP
 */
export function getErrorTitle(error: unknown): string {
    if (isAxiosError(error) && error.response?.data) {
        const data = error.response.data as ErrorResponse;

        // Usa o campo 'error' do ErrorResponse do Java
        if (data.error) {
            return data.error;
        }
    }

    if (isAxiosError(error) && error.response) {
        const status = error.response.status;

        switch (status) {
            case 400: return "Dados Inválidos";
            case 401: return "Não Autorizado";
            case 403: return "Acesso Negado";
            case 404: return "Não Encontrado";
            case 409: return "Conflito";
            case 500: return "Erro no Servidor";
            default: return "Erro";
        }
    }

    return "Erro";
}

/**
 * Type guard para AxiosError
 */
function isAxiosError(error: unknown): error is AxiosError {
    return (error as AxiosError)?.isAxiosError === true;
}

/**
 * Extrai informações completas do erro
 */
export function parseError(error: unknown): {
    title: string;
    message: string;
    status?: number;
    path?: string;
} {
    const title = getErrorTitle(error);
    const message = getErrorMessage(error);

    let status: number | undefined;
    let path: string | undefined;

    if (isAxiosError(error) && error.response?.data) {
        const data = error.response.data as ErrorResponse;
        status = data.status || error.response.status;
        path = data.path;
    }

    return { title, message, status, path };
}