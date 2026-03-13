// pages/Contratos.tsx
import { useEffect, useState, useMemo } from "react";
import { contratoService } from "@/services/api";
import { ContratoResponse, StatusContrato } from "@/types";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select";
import {
    Tooltip,
    TooltipContent,
    TooltipProvider,
    TooltipTrigger,
} from "@/components/ui/tooltip";
import {
    Search,
    Ban,
    FileText,
    Calendar,
    DollarSign,
    AlertTriangle,
    CheckCircle2,
    XCircle,
    Filter,
    X,
} from "lucide-react";
import { NovoContratoModal } from "@/components/modals/NovoContratoModal";
import { StatCard } from "@/components/ui/stat-card";
import { TableSkeleton } from "@/components/ui/table-skeleton";
import { EmptyState } from "@/components/ui/empty-state";

const STATUS_OPTIONS: { value: StatusContrato | "TODOS"; label: string }[] = [
    { value: "TODOS", label: "Todos os Status" },
    { value: "ATIVO", label: "Ativo" },
    { value: "FINALIZADO", label: "Finalizado" },
    { value: "CANCELADO", label: "Cancelado" },
    { value: "EM_DISPUTA", label: "Em Disputa" },
];

const STATUS_CONFIG: Record<StatusContrato, {
    className: string;
    icon: typeof CheckCircle2;
}> = {
    ATIVO: {
        className: "bg-green-600 hover:bg-green-700 text-white",
        icon: CheckCircle2
    },
    FINALIZADO: {
        className: "bg-gray-600 hover:bg-gray-700 text-white",
        icon: FileText
    },
    CANCELADO: {
        className: "bg-red-600 hover:bg-red-700 text-white",
        icon: XCircle
    },
    EM_DISPUTA: {
        className: "bg-yellow-600 hover:bg-yellow-700 text-white",
        icon: AlertTriangle
    },
};

export function Contratos() {
    const [contratos, setContratos] = useState<ContratoResponse[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState("");
    const [statusFilter, setStatusFilter] = useState<StatusContrato | "TODOS">("TODOS");

    const carregarDados = async () => {
        setIsLoading(true);
        try {
            const res = await contratoService.listar();
            setContratos(res.data);
        } catch (error) {
            console.error("Erro ao carregar contratos:", error);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        carregarDados();
    }, []);

    // Dados filtrados
    const contratosFiltrados = useMemo(() => {
        return contratos.filter((contrato) => {
            const matchesSearch = contrato.nomeInquilino
                .toLowerCase()
                .includes(searchTerm.toLowerCase());
            const matchesStatus = statusFilter === "TODOS" || contrato.statusContrato === statusFilter;
            return matchesSearch && matchesStatus;
        });
    }, [contratos, searchTerm, statusFilter]);

    // Estatísticas
    const stats = useMemo(() => {
        const ativos = contratos.filter((c) => c.statusContrato === "ATIVO");
        const valorTotal = ativos.reduce((acc, c) => acc + c.valorAcordado, 0);
        const emDisputa = contratos.filter((c) => c.statusContrato === "EM_DISPUTA").length;

        return {
            total: contratos.length,
            ativos: ativos.length,
            valorTotal,
            emDisputa,
        };
    }, [contratos]);

    const handleCancelar = async (id: number) => {
        if (confirm("Deseja realmente CANCELAR este contrato?")) {
            try {
                await contratoService.atualizarStatus(id, "CANCELADO");
                carregarDados();
            } catch (error) {
                alert("Erro ao atualizar status no servidor: " + error);
            }
        }
    };

    const limparFiltros = () => {
        setSearchTerm("");
        setStatusFilter("TODOS");
    };

    const hasActiveFilters = searchTerm !== "" || statusFilter !== "TODOS";

    return (
        <TooltipProvider>
            <div className="space-y-8">
                {/* Header */}
                <header className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
                    <div>
                        <h1 className="text-3xl font-bold tracking-tight text-white">
                            Gestão de Contratos
                        </h1>
                        <p className="text-muted-foreground font-medium">
                            Gerencie contratos de locação e acompanhe o status em tempo real.
                        </p>
                    </div>
                    <NovoContratoModal onSucesso={carregarDados} />
                </header>

                {/* Estatísticas */}
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                    <StatCard
                        title="Total de Contratos"
                        value={stats.total}
                        icon={FileText}
                        variant="default"
                    />
                    <StatCard
                        title="Contratos Ativos"
                        value={stats.ativos}
                        icon={CheckCircle2}
                        variant="success"
                    />
                    <StatCard
                        title="Receita Mensal"
                        value={`R$ ${stats.valorTotal.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}`}
                        icon={DollarSign}
                        variant="success"
                    />
                    <StatCard
                        title="Em Disputa"
                        value={stats.emDisputa}
                        icon={AlertTriangle}
                        variant={stats.emDisputa > 0 ? "warning" : "default"}
                    />
                </div>

                {/* Tabela */}
                <Card className="border-border/50 bg-card/30 backdrop-blur-sm">
                    <CardHeader>
                        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
                            <div>
                                <CardTitle className="text-xl flex items-center gap-2 text-white">
                                    <FileText className="h-5 w-5 text-primary" />
                                    Histórico de Contratos
                                </CardTitle>
                                <CardDescription className="font-medium">
                                    {contratosFiltrados.length} de {contratos.length} contrato(s)
                                </CardDescription>
                            </div>

                            {/* Filtros */}
                            <div className="flex flex-col sm:flex-row gap-3 w-full sm:w-auto">
                                <div className="relative flex-1 sm:w-64">
                                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                                    <Input
                                        placeholder="Buscar por inquilino..."
                                        value={searchTerm}
                                        onChange={(e) => setSearchTerm(e.target.value)}
                                        className="pl-9 bg-background/50 border-border/50"
                                    />
                                </div>
                                <Select
                                    value={statusFilter}
                                    onValueChange={(value) => setStatusFilter(value as StatusContrato | "TODOS")}
                                >
                                    <SelectTrigger className="w-full sm:w-44 bg-background/50 border-border/50">
                                        <Filter className="h-4 w-4 mr-2" />
                                        <SelectValue />
                                    </SelectTrigger>
                                    <SelectContent>
                                        {STATUS_OPTIONS.map((option) => (
                                            <SelectItem key={option.value} value={option.value}>
                                                {option.label}
                                            </SelectItem>
                                        ))}
                                    </SelectContent>
                                </Select>
                                {hasActiveFilters && (
                                    <Tooltip>
                                        <TooltipTrigger asChild>
                                            <Button
                                                variant="ghost"
                                                size="icon"
                                                onClick={limparFiltros}
                                                className="shrink-0"
                                            >
                                                <X className="h-4 w-4" />
                                            </Button>
                                        </TooltipTrigger>
                                        <TooltipContent>Limpar filtros</TooltipContent>
                                    </Tooltip>
                                )}
                            </div>
                        </div>
                    </CardHeader>
                    <CardContent>
                        <div className="rounded-lg border border-border/40 overflow-hidden">
                            <Table>
                                <TableHeader>
                                    <TableRow className="hover:bg-transparent border-border/60 bg-muted/30">
                                        <TableHead className="font-semibold py-4 text-gray-400">
                                            Inquilino
                                        </TableHead>
                                        <TableHead className="font-semibold text-gray-400">
                                            Imóvel
                                        </TableHead>
                                        <TableHead className="font-semibold text-gray-400">
                                            Tipo
                                        </TableHead>
                                        <TableHead className="font-semibold text-gray-400">
                                            Status
                                        </TableHead>
                                        <TableHead className="font-semibold text-gray-400">
                                            Período
                                        </TableHead>
                                        <TableHead className="text-right font-semibold text-gray-400">
                                            Valor
                                        </TableHead>
                                        <TableHead className="text-right font-semibold text-gray-400">
                                            Ações
                                        </TableHead>
                                    </TableRow>
                                </TableHeader>
                                <TableBody>
                                    {isLoading ? (
                                        <TableSkeleton columns={7} rows={5} />
                                    ) : contratosFiltrados.length === 0 ? (
                                        <TableRow>
                                            <TableCell colSpan={7}>
                                                <EmptyState
                                                    icon={FileText}
                                                    title={hasActiveFilters ? "Nenhum resultado encontrado" : "Nenhum contrato cadastrado"}
                                                    description={
                                                        hasActiveFilters
                                                            ? "Tente ajustar os filtros para encontrar o que procura."
                                                            : "Comece cadastrando um novo contrato de locação."
                                                    }
                                                    action={
                                                        hasActiveFilters ? (
                                                            <Button variant="outline" onClick={limparFiltros}>
                                                                Limpar Filtros
                                                            </Button>
                                                        ) : (
                                                            <NovoContratoModal onSucesso={carregarDados} />
                                                        )
                                                    }
                                                />
                                            </TableCell>
                                        </TableRow>
                                    ) : (
                                        contratosFiltrados.map((c) => {
                                            const statusConfig = STATUS_CONFIG[c.statusContrato];
                                            const StatusIcon = statusConfig.icon;

                                            return (
                                                <TableRow
                                                    key={c.id}
                                                    className="border-border/40 hover:bg-muted/20 transition-colors"
                                                >
                                                    <TableCell className="font-medium text-white">
                                                        {c.nomeInquilino}
                                                    </TableCell>
                                                    <TableCell className="text-muted-foreground font-mono">
                                                        #{c.imovelId}
                                                    </TableCell>
                                                    <TableCell>
                                                        <Badge variant="outline" className="text-xs font-medium">
                                                            {c.tipo}
                                                        </Badge>
                                                    </TableCell>
                                                    <TableCell>
                                                        <Badge className={`font-bold px-3 py-0.5 gap-1 ${statusConfig.className}`}>
                                                            <StatusIcon className="h-3 w-3" />
                                                            {c.statusContrato.replace("_", " ")}
                                                        </Badge>
                                                    </TableCell>
                                                    <TableCell className="text-muted-foreground">
                                                        <div className="flex items-center gap-2">
                                                            <Calendar className="h-4 w-4" />
                                                            {new Date(c.dataInicio).toLocaleDateString("pt-BR", {
                                                                timeZone: "UTC",
                                                            })}
                                                        </div>
                                                    </TableCell>
                                                    <TableCell className="text-right font-bold tabular-nums text-white">
                                                        R$ {c.valorAcordado.toLocaleString("pt-BR", {
                                                            minimumFractionDigits: 2,
                                                        })}
                                                    </TableCell>
                                                    <TableCell className="text-right">
                                                        {c.statusContrato === "ATIVO" && (
                                                            <Tooltip>
                                                                <TooltipTrigger asChild>
                                                                    <Button
                                                                        variant="ghost"
                                                                        size="icon"
                                                                        className="text-red-400 hover:text-red-300 hover:bg-red-400/10"
                                                                        onClick={() => handleCancelar(c.id)}
                                                                    >
                                                                        <Ban className="h-4 w-4" />
                                                                    </Button>
                                                                </TooltipTrigger>
                                                                <TooltipContent>Cancelar Contrato</TooltipContent>
                                                            </Tooltip>
                                                        )}
                                                    </TableCell>
                                                </TableRow>
                                            );
                                        })
                                    )}
                                </TableBody>
                            </Table>
                        </div>
                    </CardContent>
                </Card>
            </div>
        </TooltipProvider>
    );
}

export default Contratos;