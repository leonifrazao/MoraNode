// pages/Dashboard.tsx
import { useEffect, useState, useMemo } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Progress } from "@/components/ui/progress";
import { imovelService, contratoService } from "@/services/api";
import { ImovelResponse, ContratoResponse, StatusContrato } from "@/types";
import {
    Building2,
    DollarSign,
    FileCheck,
    Percent,
    TrendingUp,
    Home,
    FileText,
    ArrowRight,
    CheckCircle2,
    XCircle,
    AlertTriangle,
    Clock,
    RefreshCw,
} from "lucide-react";
import { Link } from "react-router-dom";
import { StatCard } from "@/components/ui/stat-card";
import { TableSkeleton } from "@/components/ui/table-skeleton";
import { EmptyState } from "@/components/ui/empty-state";
import { Skeleton } from "@/components/ui/skeleton";

const STATUS_CONFIG: Record<StatusContrato, { className: string; icon: typeof CheckCircle2 }> = {
    ATIVO: { className: "bg-green-600 text-white", icon: CheckCircle2 },
    FINALIZADO: { className: "bg-gray-600 text-white", icon: FileText },
    CANCELADO: { className: "bg-red-600 text-white", icon: XCircle },
    EM_DISPUTA: { className: "bg-yellow-600 text-white", icon: AlertTriangle },
};

function StatCardSkeleton() {
    return (
        <Card className="border-border/50 bg-card/30">
            <CardContent className="p-6">
                <div className="flex items-center justify-between">
                    <div className="space-y-2">
                        <Skeleton className="h-4 w-24" />
                        <Skeleton className="h-8 w-32" />
                        <Skeleton className="h-3 w-20" />
                    </div>
                    <Skeleton className="h-12 w-12 rounded-full" />
                </div>
            </CardContent>
        </Card>
    );
}

export default function Dashboard() {
    const [imoveis, setImoveis] = useState<ImovelResponse[]>([]);
    const [contratos, setContratos] = useState<ContratoResponse[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [lastUpdate, setLastUpdate] = useState<Date>(new Date());

    const fetchData = async () => {
        setIsLoading(true);
        try {
            const [resImoveis, resContratos] = await Promise.all([
                imovelService.listar(),
                contratoService.listar()
            ]);
            setImoveis(resImoveis.data);
            setContratos(resContratos.data);
            setLastUpdate(new Date());
        } catch (error) {
            console.error("Erro ao carregar dados do dashboard:", error);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);

    // Métricas calculadas
    const metrics = useMemo(() => {
        const contratosAtivos = contratos.filter(c => c.statusContrato === "ATIVO");
        const totalReceita = contratosAtivos.reduce((acc, c) => acc + c.valorAcordado, 0);

        const imoveisOcupados = imoveis.filter(i => !i.disponivel).length;
        const taxaOcupacao = imoveis.length > 0
            ? (imoveisOcupados / imoveis.length) * 100
            : 0;

        const contratosEmDisputa = contratos.filter(c => c.statusContrato === "EM_DISPUTA").length;
        const contratosCancelados = contratos.filter(c => c.statusContrato === "CANCELADO").length;

        const valorMedioContrato = contratosAtivos.length > 0
            ? totalReceita / contratosAtivos.length
            : 0;

        const areaTotal = imoveis.reduce((acc, i) => acc + i.metrosQuadrados, 0);
        const valorTotalImoveis = imoveis.reduce((acc, i) => acc + i.valor, 0);

        return {
            totalReceita,
            taxaOcupacao,
            imoveisOcupados,
            imoveisDisponiveis: imoveis.length - imoveisOcupados,
            contratosAtivos: contratosAtivos.length,
            contratosEmDisputa,
            contratosCancelados,
            valorMedioContrato,
            areaTotal,
            valorTotalImoveis,
        };
    }, [imoveis, contratos]);

    // Últimos 5 itens de cada
    const ultimosImoveis = useMemo(() => imoveis.slice(-5).reverse(), [imoveis]);
    const ultimosContratos = useMemo(() => contratos.slice(-5).reverse(), [contratos]);

    return (
        <div className="space-y-8">
            {/* Header */}
            <header className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
                <div>
                    <h1 className="text-2xl md:text-3xl font-bold tracking-tight text-white">Dashboard</h1>
                    <p className="text-muted-foreground font-medium">
                        Bem-vindo ao MoraNode. Visão geral do seu portfólio.
                    </p>
                </div>
                <div className="flex items-center gap-3">
                    <span className="text-xs text-muted-foreground flex items-center gap-1">
                        <Clock className="h-3 w-3" />
                        Atualizado: {lastUpdate.toLocaleTimeString("pt-BR")}
                    </span>
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={fetchData}
                        disabled={isLoading}
                        className="gap-2"
                    >
                        <RefreshCw className={`h-4 w-4 ${isLoading ? "animate-spin" : ""}`} />
                        Atualizar
                    </Button>
                </div>
            </header>

            {/* Cards de Métricas Principais */}
            {isLoading ? (
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                    {Array.from({ length: 4 }).map((_, i) => (
                        <StatCardSkeleton key={i} />
                    ))}
                </div>
            ) : (
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                    <StatCard
                        title="Receita Mensal Ativa"
                        value={`R$ ${metrics.totalReceita.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}`}
                        description={`${metrics.contratosAtivos} contrato(s) ativo(s)`}
                        icon={DollarSign}
                        variant="success"
                    />
                    <StatCard
                        title="Imóveis no Portfólio"
                        value={imoveis.length}
                        description={`${metrics.imoveisDisponiveis} disponível(is)`}
                        icon={Building2}
                        variant="default"
                    />
                    <StatCard
                        title="Contratos Totais"
                        value={contratos.length}
                        description={metrics.contratosEmDisputa > 0 ? `${metrics.contratosEmDisputa} em disputa` : "Todos regulares"}
                        icon={FileCheck}
                        variant={metrics.contratosEmDisputa > 0 ? "warning" : "default"}
                    />
                    <StatCard
                        title="Taxa de Ocupação"
                        value={`${metrics.taxaOcupacao.toFixed(0)}%`}
                        description={`${metrics.imoveisOcupados} de ${imoveis.length} ocupado(s)`}
                        icon={Percent}
                        variant={metrics.taxaOcupacao >= 80 ? "success" : metrics.taxaOcupacao >= 50 ? "warning" : "danger"}
                    />
                </div>
            )}

            {/* Barra de Progresso de Ocupação */}
            {!isLoading && imoveis.length > 0 && (
                <Card className="border-border/50 bg-card/30 backdrop-blur-sm">
                    <CardContent className="pt-6">
                        <div className="flex items-center justify-between mb-2">
                            <span className="text-sm font-medium text-muted-foreground">
                                Ocupação do Portfólio
                            </span>
                            <span className="text-sm font-bold text-white">
                                {metrics.taxaOcupacao.toFixed(1)}%
                            </span>
                        </div>
                        <Progress
                            value={metrics.taxaOcupacao}
                            className="h-2"
                        />
                        <div className="flex justify-between mt-2 text-xs text-muted-foreground">
                            <span>{metrics.imoveisOcupados} ocupado(s)</span>
                            <span>{metrics.imoveisDisponiveis} disponível(is)</span>
                        </div>
                    </CardContent>
                </Card>
            )}

            {/* Grid de Métricas Secundárias */}
            {!isLoading && (
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <Card className="border-border/50 bg-card/30 backdrop-blur-sm">
                        <CardContent className="pt-6">
                            <div className="flex items-center gap-3">
                                <div className="p-2 rounded-lg bg-blue-500/10">
                                    <TrendingUp className="h-5 w-5 text-blue-500" />
                                </div>
                                <div>
                                    <p className="text-xs text-muted-foreground">Ticket Médio</p>
                                    <p className="text-lg font-bold text-white">
                                        R$ {metrics.valorMedioContrato.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                                    </p>
                                </div>
                            </div>
                        </CardContent>
                    </Card>
                    <Card className="border-border/50 bg-card/30 backdrop-blur-sm">
                        <CardContent className="pt-6">
                            <div className="flex items-center gap-3">
                                <div className="p-2 rounded-lg bg-purple-500/10">
                                    <Home className="h-5 w-5 text-purple-500" />
                                </div>
                                <div>
                                    <p className="text-xs text-muted-foreground">Área Total</p>
                                    <p className="text-lg font-bold text-white">
                                        {metrics.areaTotal.toLocaleString("pt-BR")} m²
                                    </p>
                                </div>
                            </div>
                        </CardContent>
                    </Card>
                    <Card className="border-border/50 bg-card/30 backdrop-blur-sm">
                        <CardContent className="pt-6">
                            <div className="flex items-center gap-3">
                                <div className="p-2 rounded-lg bg-green-500/10">
                                    <DollarSign className="h-5 w-5 text-green-500" />
                                </div>
                                <div>
                                    <p className="text-xs text-muted-foreground">Valor Patrimonial</p>
                                    <p className="text-lg font-bold text-white">
                                        R$ {metrics.valorTotalImoveis.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                                    </p>
                                </div>
                            </div>
                        </CardContent>
                    </Card>
                </div>
            )}

            {/* Tabelas lado a lado */}
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                {/* Últimos Imóveis */}
                <Card className="border-border/50 bg-card/30 backdrop-blur-sm">
                    <CardHeader className="flex flex-row items-center justify-between">
                        <div>
                            <CardTitle className="text-lg flex items-center gap-2 text-white">
                                <Home className="h-5 w-5 text-primary" />
                                Últimos Imóveis
                            </CardTitle>
                            <CardDescription className="font-medium">
                                Adições recentes ao portfólio
                            </CardDescription>
                        </div>
                        <Button variant="ghost" size="sm" asChild className="gap-1">
                            <Link to="/imoveis">
                                Ver todos <ArrowRight className="h-4 w-4" />
                            </Link>
                        </Button>
                    </CardHeader>
                    <CardContent>
                        {isLoading ? (
                            <div className="rounded-lg border border-border/40 overflow-hidden">
                                <Table>
                                    <TableBody>
                                        <TableSkeleton columns={3} rows={3} />
                                    </TableBody>
                                </Table>
                            </div>
                        ) : ultimosImoveis.length === 0 ? (
                            <EmptyState
                                icon={Building2}
                                title="Nenhum imóvel"
                                description="Cadastre seu primeiro imóvel para começar."
                            />
                        ) : (
                            <div className="rounded-lg border border-border/40 overflow-hidden">
                                <Table>
                                    <TableHeader>
                                        <TableRow className="hover:bg-transparent border-border/60 bg-muted/30">
                                            <TableHead className="font-semibold py-3 text-gray-400">Status</TableHead>
                                            <TableHead className="font-semibold text-gray-400">Área</TableHead>
                                            <TableHead className="text-right font-semibold text-gray-400">Valor</TableHead>
                                        </TableRow>
                                    </TableHeader>
                                    <TableBody>
                                        {ultimosImoveis.map((imovel) => (
                                            <TableRow
                                                key={imovel.id}
                                                className="border-border/40 hover:bg-muted/20 transition-colors"
                                            >
                                                <TableCell>
                                                    <Badge className={`font-bold px-2 py-0.5 text-xs gap-1 ${imovel.disponivel
                                                        ? "bg-green-600 text-white"
                                                        : "bg-yellow-600 text-white"
                                                        }`}>
                                                        {imovel.disponivel ? (
                                                            <><CheckCircle2 className="h-3 w-3" /> LIVRE</>
                                                        ) : (
                                                            <><XCircle className="h-3 w-3" /> OCUPADO</>
                                                        )}
                                                    </Badge>
                                                </TableCell>
                                                <TableCell className="text-muted-foreground font-medium">
                                                    {imovel.metrosQuadrados} m²
                                                </TableCell>
                                                <TableCell className="text-right font-bold tabular-nums text-white">
                                                    R$ {imovel.valor.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                                                </TableCell>
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>
                            </div>
                        )}
                    </CardContent>
                </Card>

                {/* Últimos Contratos */}
                <Card className="border-border/50 bg-card/30 backdrop-blur-sm">
                    <CardHeader className="flex flex-row items-center justify-between">
                        <div>
                            <CardTitle className="text-lg flex items-center gap-2 text-white">
                                <FileText className="h-5 w-5 text-primary" />
                                Últimos Contratos
                            </CardTitle>
                            <CardDescription className="font-medium">
                                Contratos mais recentes
                            </CardDescription>
                        </div>
                        <Button variant="ghost" size="sm" asChild className="gap-1">
                            <Link to="/contratos">
                                Ver todos <ArrowRight className="h-4 w-4" />
                            </Link>
                        </Button>
                    </CardHeader>
                    <CardContent>
                        {isLoading ? (
                            <div className="rounded-lg border border-border/40 overflow-hidden">
                                <Table>
                                    <TableBody>
                                        <TableSkeleton columns={3} rows={3} />
                                    </TableBody>
                                </Table>
                            </div>
                        ) : ultimosContratos.length === 0 ? (
                            <EmptyState
                                icon={FileCheck}
                                title="Nenhum contrato"
                                description="Crie seu primeiro contrato para começar."
                            />
                        ) : (
                            <div className="rounded-lg border border-border/40 overflow-hidden">
                                <Table>
                                    <TableHeader>
                                        <TableRow className="hover:bg-transparent border-border/60 bg-muted/30">
                                            <TableHead className="font-semibold py-3 text-gray-400">Inquilino</TableHead>
                                            <TableHead className="font-semibold text-gray-400">Status</TableHead>
                                            <TableHead className="text-right font-semibold text-gray-400">Valor</TableHead>
                                        </TableRow>
                                    </TableHeader>
                                    <TableBody>
                                        {ultimosContratos.map((contrato) => {
                                            const config = STATUS_CONFIG[contrato.statusContrato];
                                            const StatusIcon = config.icon;
                                            return (
                                                <TableRow
                                                    key={contrato.id}
                                                    className="border-border/40 hover:bg-muted/20 transition-colors"
                                                >
                                                    <TableCell className="font-medium text-white truncate max-w-[120px]">
                                                        {contrato.nomeInquilino}
                                                    </TableCell>
                                                    <TableCell>
                                                        <Badge className={`font-bold px-2 py-0.5 text-xs gap-1 ${config.className}`}>
                                                            <StatusIcon className="h-3 w-3" />
                                                            {contrato.statusContrato.replace("_", " ")}
                                                        </Badge>
                                                    </TableCell>
                                                    <TableCell className="text-right font-bold tabular-nums text-white">
                                                        R$ {contrato.valorAcordado.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                                                    </TableCell>
                                                </TableRow>
                                            );
                                        })}
                                    </TableBody>
                                </Table>
                            </div>
                        )}
                    </CardContent>
                </Card>
            </div>

            {/* Quick Actions */}
            <Card className="border-border/50 bg-gradient-to-r from-primary/10 to-purple-500/10 backdrop-blur-sm">
                <CardContent className="pt-6">
                    <div className="flex flex-col sm:flex-row items-center justify-between gap-4">
                        <div>
                            <h3 className="text-lg font-semibold text-white">Ações Rápidas</h3>
                            <p className="text-sm text-muted-foreground">
                                Gerencie seu portfólio de forma eficiente
                            </p>
                        </div>
                        <div className="flex gap-3">
                            <Button asChild variant="outline" className="gap-2">
                                <Link to="/imoveis">
                                    <Building2 className="h-4 w-4" />
                                    Gerenciar Imóveis
                                </Link>
                            </Button>
                            <Button asChild className="gap-2 bg-white text-black hover:bg-gray-200">
                                <Link to="/contratos">
                                    <FileText className="h-4 w-4" />
                                    Gerenciar Contratos
                                </Link>
                            </Button>
                        </div>
                    </div>
                </CardContent>
            </Card>
        </div>
    );
}