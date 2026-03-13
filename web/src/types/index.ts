// src/types/index.ts

// ============================================
// ENUMS (espelhando o Java)
// ============================================
export type StatusContrato = "ATIVO" | "FINALIZADO" | "EM_DISPUTA" | "CANCELADO";
export type TipoContrato = "VENDA" | "ALUGUEL";

// ============================================
// RESPONSES (O que vem do Backend)
// ============================================

// Espelha: ImovelResponse.java
// NOTA: O Java retorna int para valor e metrosQuadrados, e NÃO retorna endereco!
export interface ImovelResponse {
    id: number;
    valor: number;           // int no Java
    metrosQuadrados: number; // int no Java
    disponivel: boolean;
    endereco: string;
}

export interface ErrorResponse {
    timestamp: string;
    status: number;
    error: string;
    message: string;
    path: string;
}

// Espelha: ContratoResponse.java
export interface ContratoResponse {
    id: number;
    imovelId: number;
    nomeInquilino: string;
    valorAcordado: number;      // BigDecimal vira number no JS
    dataInicio: string;         // LocalDate vira string ISO (yyyy-MM-dd)
    dataFim: string | null;     // Pode ser null
    statusContrato: StatusContrato;
    tipo: TipoContrato;
    podeRenovar: boolean;
}

// ============================================
// REQUESTS (O que enviamos para o Backend)
// ============================================

// Espelha: ImovelRequest.java
export interface ImovelRequest {
    valor: number;           // @Positive int
    endereco: string;        // @NotBlank String
    metrosQuadrados: number; // @Positive int
    disponivel: boolean;
}

// Espelha: ContratoRequest.java
export interface ContratoRequest {
    imovelId: number;            // @NotNull Long
    nomeDono: string;            // @NotBlank String
    nomeInquilino: string;       // @NotBlank String
    valorAcordado: number;       // @NotNull @Positive BigDecimal
    dataInicio: string;          // @NotNull @FutureOrPresent LocalDate (formato: yyyy-MM-dd)
    dataFim?: string | null;     // LocalDate opcional
    podeRenovar: boolean;
    taxaJurosMensal: number;     // @NotNull @PositiveOrZero BigDecimal
    tipo: TipoContrato;          // @NotNull TipoContrato
    statusContrato: StatusContrato; // @NotNull StatusContrato
}

// Espelha: StatusRequest.java
export interface StatusRequest {
    statusContrato: StatusContrato;
}

// ============================================
// AUTHENTICATION (Login, Registro, Tokens)
// ============================================

export interface LoginRequest {
    email: string;
    senha: string;
}

export interface RegistroRequest {
    nome: string;
    email: string;
    senha: string;
}

export interface TokenResponse {
    accessToken: string;
    refreshToken: string;
}

export interface JwtPayload {
    sub: string;     // email
    papel: string;   // role (USUARIO, ADMIN)
    exp: number;     // expiration time
    iat: number;     // issued at
}