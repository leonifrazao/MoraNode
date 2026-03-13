// pages/Imoveis.tsx
import { useEffect, useState, useMemo } from "react";
import { imovelService } from "@/services/api";
import { ImovelResponse } from "@/types";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {
    Search,
    Trash2,
    Home,
    Building2,
    DollarSign,
    CheckCircle2,
    XCircle,
    Filter,
    X,
    Maximize,
    Loader2,
    AlertCircle,
    MoreHorizontal,
    Pencil,
} from "lucide-react";
import { NovoImovelModal } from "@/components/modals/NovoImovelModal";
import { EditarImovelModal } from "@/components/modals/EditarImovelModal";
import { StatCard } from "@/components/ui/stat-card";
import { TableSkeleton } from "@/components/ui/table-skeleton";
import { EmptyState } from "@/components/ui/empty-state";
import { cn } from "@/lib/utils";
import { parseError } from "@/lib/error-utils";

type StatusFilter = "TODOS" | "DISPONIVEL" | "OCUPADO";

type DeleteDialogState =
    | { type: "closed" }
    | { type: "confirm"; imovel: ImovelResponse }
    | { type: "loading"; imovel: ImovelResponse }
    | { type: "error"; imovel: ImovelResponse; error: { title: string; message: string } };

const STATUS_OPTIONS: { value: StatusFilter; label: string }[] = [
    { value: "TODOS", label: "Todos os Status" },
    { value: "DISPONIVEL", label: "Disponível" },
    { value: "OCUPADO", label: "Ocupado" },
];

export function Imoveis() {
    const [imoveis, setImoveis] = useState<ImovelResponse[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState("");
    const [statusFilter, setStatusFilter] = useState<StatusFilter>("TODOS");

    // Estados dos modais
    const [deleteDialog, setDeleteDialog] = useState<DeleteDialogState>({ type: "closed" });
    const [editModal, setEditModal] = useState<{ open: boolean; imovel: ImovelResponse | null }>({
        open: false,
        imovel: null,
    });

    const carregarDados = async () => {
        setIsLoading(true);
        try {
            const res = await imovelService.listar();
            setImoveis(res.data);
        } catch (error) {
            console.error("Erro ao carregar imóveis:", error);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        carregarDados();
    }, []);

    // Dados filtrados
    const imoveisFiltrados = useMemo(() => {
        return imoveis.filter((imovel) => {
            const matchesSearch = (imovel.endereco || "")
                .toLowerCase()
                .includes(searchTerm.toLowerCase());

            let matchesStatus = true;
            if (statusFilter === "DISPONIVEL") {
                matchesStatus = imovel.disponivel;
            } else if (statusFilter === "OCUPADO") {
                matchesStatus = !imovel.disponivel;
            }

            return matchesSearch && matchesStatus;
        });
    }, [imoveis, searchTerm, statusFilter]);

    // Estatísticas
    const stats = useMemo(() => {
        const disponiveis = imoveis.filter((i) => i.disponivel);
        const ocupados = imoveis.filter((i) => !i.disponivel);
        const valorTotal = imoveis.reduce((acc, i) => acc + i.valor, 0);
        const areaTotal = imoveis.reduce((acc, i) => acc + i.metrosQuadrados, 0);

        return {
            total: imoveis.length,
            disponiveis: disponiveis.length,
            ocupados: ocupados.length,
            valorTotal,
            areaTotal,
        };
    }, [imoveis]);

    // ========== HANDLERS ==========

    // Editar
    const handleEditClick = (imovel: ImovelResponse) => {
        setEditModal({ open: true, imovel });
    };

    const handleEditClose = () => {
        setEditModal({ open: false, imovel: null });
    };

    const handleEditSuccess = () => {
        carregarDados();
    };

    // Deletar
    const handleDeleteClick = (imovel: ImovelResponse) => {
        setDeleteDialog({ type: "confirm", imovel });
    };

    const handleDeleteConfirm = async () => {
        if (deleteDialog.type !== "confirm" && deleteDialog.type !== "error") return;

        const imovel = deleteDialog.imovel;
        setDeleteDialog({ type: "loading", imovel });

        try {
            await imovelService.deletar(imovel.id);
            setDeleteDialog({ type: "closed" });
            carregarDados();
        } catch (error) {
            const { title, message } = parseError(error);
            setDeleteDialog({
                type: "error",
                imovel,
                error: { title, message },
            });
        }
    };

    const handleDeleteDialogClose = () => {
        if (deleteDialog.type !== "loading") {
            setDeleteDialog({ type: "closed" });
        }
    };

    const limparFiltros = () => {
        setSearchTerm("");
        setStatusFilter("TODOS");
    };

    const hasActiveFilters = searchTerm !== "" || statusFilter !== "TODOS";
    const isDeleteDialogOpen = deleteDialog.type !== "closed";

    return (
        <>
            <div className="space-y-8">
                {/* Header */}
                <header className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
                    <div>
                        <h1 className="text-3xl font-bold tracking-tight text-white">
                            Gestão de Imóveis
                        </h1>
                        <p className="text-muted-foreground font-medium">
                            Visualize e gerencie seu portfólio imobiliário.
                        </p>
                    </div>
                    <NovoImovelModal onSucesso={carregarDados} />
                </header>

                {/* Estatísticas */}
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
                    <StatCard
                        title="Total de Imóveis"
                        value={stats.total}
                        icon={Building2}
                        variant="default"
                    />
                    <StatCard
                        title="Disponíveis"
                        value={stats.disponiveis}
                        icon={CheckCircle2}
                        variant="success"
                    />
                    <StatCard
                        title="Ocupados"
                        value={stats.ocupados}
                        icon={Home}
                        variant={stats.ocupados > 0 ? "warning" : "default"}
                    />
                    <StatCard
                        title="Valor Total Mensal"
                        value={`R$ ${stats.valorTotal.toLocaleString("pt-BR")}`}
                        description={`${stats.areaTotal.toLocaleString("pt-BR")} m² total`}
                        icon={DollarSign}
                        variant="success"
                    />
                </div>

                {/* Tabela */}
                <Card className="border-border/50 bg-card/30 backdrop-blur-sm">
                    <CardHeader>
                        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
                            <div>
                                <CardTitle className="text-xl flex items-center gap-2 text-white">
                                    <Home className="h-5 w-5 text-primary" />
                                    Todos os Imóveis
                                </CardTitle>
                                <CardDescription className="font-medium">
                                    {imoveisFiltrados.length} de {imoveis.length} imóvel(is)
                                </CardDescription>
                            </div>

                            {/* Filtros */}
                            <div className="flex flex-col sm:flex-row gap-3 w-full sm:w-auto">
                                <div className="relative flex-1 sm:w-64">
                                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                                    <Input
                                        placeholder="Buscar por endereço..."
                                        value={searchTerm}
                                        onChange={(e) => setSearchTerm(e.target.value)}
                                        className="pl-9 bg-background/50 border-border/50"
                                    />
                                </div>
                                <Select
                                    value={statusFilter}
                                    onValueChange={(value) => setStatusFilter(value as StatusFilter)}
                                >
                                    <SelectTrigger className="w-full sm:w-44 bg-background/50 border-border/50">
                                        <Filter className="h-4 w-4 mr-2" />
                                        <SelectValue />
                                    </SelectTrigger>
                                    <SelectContent className="bg-[#1a1a1d] border-border">
                                        {STATUS_OPTIONS.map((option) => (
                                            <SelectItem key={option.value} value={option.value}>
                                                {option.label}
                                            </SelectItem>
                                        ))}
                                    </SelectContent>
                                </Select>
                                {hasActiveFilters && (
                                    <Button
                                        variant="ghost"
                                        size="icon"
                                        onClick={limparFiltros}
                                        className="shrink-0"
                                        title="Limpar filtros"
                                    >
                                        <X className="h-4 w-4" />
                                    </Button>
                                )}
                            </div>
                        </div>
                    </CardHeader>
                    <CardContent>
                        <div className="rounded-lg border border-border/40 overflow-hidden">
                            <Table>
                                <TableHeader>
                                    <TableRow className="hover:bg-transparent border-border/60 bg-muted/30">
                                        <TableHead className="font-semibold py-4 text-gray-400">ID</TableHead>
                                        <TableHead className="font-semibold text-gray-400">Endereço</TableHead>
                                        <TableHead className="font-semibold text-gray-400">Status</TableHead>
                                        <TableHead className="font-semibold text-gray-400">Área</TableHead>
                                        <TableHead className="text-right font-semibold text-gray-400">Valor Mensal</TableHead>
                                        <TableHead className="text-right font-semibold text-gray-400">Ações</TableHead>
                                    </TableRow>
                                </TableHeader>
                                <TableBody>
                                    {isLoading ? (
                                        <TableSkeleton columns={6} rows={5} />
                                    ) : imoveisFiltrados.length === 0 ? (
                                        <TableRow>
                                            <TableCell colSpan={6}>
                                                <EmptyState
                                                    icon={Building2}
                                                    title={hasActiveFilters ? "Nenhum resultado" : "Nenhum imóvel"}
                                                    description={
                                                        hasActiveFilters
                                                            ? "Tente ajustar os filtros."
                                                            : "Cadastre seu primeiro imóvel."
                                                    }
                                                    action={
                                                        hasActiveFilters ? (
                                                            <Button variant="outline" onClick={limparFiltros}>
                                                                Limpar Filtros
                                                            </Button>
                                                        ) : (
                                                            <NovoImovelModal onSucesso={carregarDados} />
                                                        )
                                                    }
                                                />
                                            </TableCell>
                                        </TableRow>
                                    ) : (
                                        imoveisFiltrados.map((imovel) => (
                                            <TableRow
                                                key={imovel.id}
                                                className="border-border/40 hover:bg-muted/20 transition-colors"
                                            >
                                                <TableCell className="font-mono text-muted-foreground">
                                                    #{imovel.id}
                                                </TableCell>
                                                <TableCell className="font-medium text-white max-w-[300px] truncate">
                                                    {imovel.endereco || "Endereço não informado"}
                                                </TableCell>
                                                <TableCell>
                                                    <Badge
                                                        className={cn(
                                                            "font-bold px-3 py-0.5 gap-1",
                                                            imovel.disponivel
                                                                ? "bg-green-600 hover:bg-green-700 text-white"
                                                                : "bg-yellow-600 hover:bg-yellow-700 text-white"
                                                        )}
                                                    >
                                                        {imovel.disponivel ? (
                                                            <><CheckCircle2 className="h-3 w-3" /> DISPONÍVEL</>
                                                        ) : (
                                                            <><XCircle className="h-3 w-3" /> OCUPADO</>
                                                        )}
                                                    </Badge>
                                                </TableCell>
                                                <TableCell className="text-muted-foreground">
                                                    <div className="flex items-center gap-2">
                                                        <Maximize className="h-4 w-4" />
                                                        {imovel.metrosQuadrados} m²
                                                    </div>
                                                </TableCell>
                                                <TableCell className="text-right font-bold tabular-nums text-white">
                                                    R$ {imovel.valor.toLocaleString("pt-BR")}
                                                </TableCell>
                                                <TableCell className="text-right">
                                                    <DropdownMenu>
                                                        <DropdownMenuTrigger asChild>
                                                            <Button
                                                                variant="ghost"
                                                                size="icon"
                                                                className="h-8 w-8"
                                                            >
                                                                <MoreHorizontal className="h-4 w-4" />
                                                                <span className="sr-only">Ações</span>
                                                            </Button>
                                                        </DropdownMenuTrigger>
                                                        <DropdownMenuContent
                                                            align="end"
                                                            className="bg-[#1a1a1d] border-border"
                                                        >
                                                            <DropdownMenuItem
                                                                onClick={() => handleEditClick(imovel)}
                                                                className="gap-2 cursor-pointer"
                                                            >
                                                                <Pencil className="h-4 w-4" />
                                                                Editar
                                                            </DropdownMenuItem>
                                                            <DropdownMenuItem
                                                                onClick={() => handleDeleteClick(imovel)}
                                                                className="gap-2 cursor-pointer text-red-400 focus:text-red-400"
                                                            >
                                                                <Trash2 className="h-4 w-4" />
                                                                Excluir
                                                            </DropdownMenuItem>
                                                        </DropdownMenuContent>
                                                    </DropdownMenu>
                                                </TableCell>
                                            </TableRow>
                                        ))
                                    )}
                                </TableBody>
                            </Table>
                        </div>
                    </CardContent>
                </Card>
            </div>

            {/* Modal de Edição */}
            <EditarImovelModal
                imovel={editModal.imovel}
                open={editModal.open}
                onOpenChange={(open) => !open && handleEditClose()}
                onSucesso={handleEditSuccess}
            />

            {/* Modal de Confirmação de Exclusão */}
            <Dialog open={isDeleteDialogOpen} onOpenChange={(open) => !open && handleDeleteDialogClose()}>
                <DialogContent className="bg-[#121214] border-border/50 text-white sm:max-w-md">
                    {deleteDialog.type === "error" ? (
                        <>
                            <DialogHeader>
                                <div className="flex items-center gap-3">
                                    <div className="p-3 rounded-full bg-red-500/10">
                                        <AlertCircle className="h-6 w-6 text-red-500" />
                                    </div>
                                    <div>
                                        <DialogTitle className="text-xl text-white">
                                            {deleteDialog.error.title}
                                        </DialogTitle>
                                    </div>
                                </div>
                                <DialogDescription className="text-muted-foreground pt-4 text-base">
                                    {deleteDialog.error.message}
                                </DialogDescription>
                            </DialogHeader>
                            <DialogFooter className="mt-6">
                                <Button
                                    onClick={handleDeleteDialogClose}
                                    className="w-full sm:w-auto bg-white text-black hover:bg-gray-200"
                                >
                                    Entendi
                                </Button>
                            </DialogFooter>
                        </>
                    ) : (deleteDialog.type === "confirm" || deleteDialog.type === "loading") ? (
                        <>
                            <DialogHeader>
                                <div className="flex items-center gap-3">
                                    <div className="p-3 rounded-full bg-red-500/10">
                                        <Trash2 className="h-6 w-6 text-red-500" />
                                    </div>
                                    <div>
                                        <DialogTitle className="text-xl text-white">
                                            Excluir Imóvel
                                        </DialogTitle>
                                    </div>
                                </div>
                                <DialogDescription className="text-muted-foreground pt-4 text-base">
                                    Tem certeza que deseja excluir{" "}
                                    <strong className="text-white">
                                        "{deleteDialog.imovel.endereco || `Imóvel #${deleteDialog.imovel.id}`}"
                                    </strong>?
                                    <br />
                                    <br />
                                    <span className="text-red-400 text-sm">
                                        ⚠️ Esta ação não pode ser desfeita.
                                    </span>
                                </DialogDescription>
                            </DialogHeader>
                            <DialogFooter className="mt-6 gap-3">
                                <Button
                                    variant="ghost"
                                    onClick={handleDeleteDialogClose}
                                    disabled={deleteDialog.type === "loading"}
                                    className="border-border/50"
                                >
                                    Cancelar
                                </Button>
                                <Button
                                    onClick={handleDeleteConfirm}
                                    disabled={deleteDialog.type === "loading"}
                                    className="bg-red-600 hover:bg-red-700 text-white gap-2"
                                >
                                    {deleteDialog.type === "loading" ? (
                                        <>
                                            <Loader2 className="h-4 w-4 animate-spin" />
                                            Excluindo...
                                        </>
                                    ) : (
                                        <>
                                            <Trash2 className="h-4 w-4" />
                                            Excluir
                                        </>
                                    )}
                                </Button>
                            </DialogFooter>
                        </>
                    ) : null}
                </DialogContent>
            </Dialog>
        </>
    );
}

export default Imoveis;