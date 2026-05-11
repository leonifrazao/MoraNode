// components/modals/NovoContratoModal.tsx
import { useState, useEffect } from "react";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
    DialogFooter,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select";
import { Separator } from "@/components/ui/separator";
import {
    Plus,
    Loader2,
    FileText,
    Building2,
    User,
    Calendar,
    DollarSign,
    Percent,
    AlertCircle,
} from "lucide-react";
import { contratoService, imovelService } from "@/services/api";
import { ImovelResponse, ContratoRequest, TipoContrato } from "@/types";
import { useToastFeedback } from "@/hooks/use-toast-feedback";
import { cn } from "@/lib/utils";

interface Props {
    onSucesso: () => void;
}

const INITIAL_FORM_DATA: ContratoRequest = {
    imovelId: 0,
    nomeDono: "",
    nomeInquilino: "",
    valorAcordado: 0,
    dataInicio: new Date().toISOString().split("T")[0],
    dataFim: null,
    podeRenovar: false,
    taxaJurosMensal: 0,
    tipo: "ALUGUEL",
    statusContrato: "ATIVO",
};

export function NovoContratoModal({ onSucesso }: Props) {
    const [open, setOpen] = useState(false);
    const [loading, setLoading] = useState(false);
    const [loadingImoveis, setLoadingImoveis] = useState(false);
    const [imoveis, setImoveis] = useState<ImovelResponse[]>([]);
    const [formData, setFormData] = useState<ContratoRequest>(INITIAL_FORM_DATA);
    const [errors, setErrors] = useState<Partial<Record<keyof ContratoRequest, string>>>({});
    const toast = useToastFeedback();

    useEffect(() => {
        if (open) {
            setLoadingImoveis(true);
            imovelService.listar()
                .then(res => {
                    const disponiveis = res.data.filter(i => i.disponivel);
                    setImoveis(disponiveis);
                })
                .catch((err) => {
                toast.error(err); // ✅ Usa o tratamento de erro
            })
            .finally(() => setLoadingImoveis(false));
    }
}, [open]);

    const handleImovelChange = (imovelId: string) => {
        const id = Number(imovelId);
        const imovel = imoveis.find(i => i.id === id);

        setFormData(prev => ({
            ...prev,
            imovelId: id,
            valorAcordado: imovel?.valor || prev.valorAcordado
        }));

        if (errors.imovelId) {
            setErrors(prev => ({ ...prev, imovelId: undefined }));
        }
    };

    const validateForm = (): boolean => {
        const newErrors: Partial<Record<keyof ContratoRequest, string>> = {};

        if (!formData.imovelId || formData.imovelId === 0) {
            newErrors.imovelId = "Selecione um imóvel";
        }
        if (!formData.nomeDono.trim()) {
            newErrors.nomeDono = "Nome do proprietário é obrigatório";
        }
        if (!formData.nomeInquilino.trim()) {
            newErrors.nomeInquilino = "Nome do inquilino é obrigatório";
        }
        if (!formData.valorAcordado || formData.valorAcordado <= 0) {
            newErrors.valorAcordado = "Valor deve ser maior que zero";
        }
        if (!formData.dataInicio) {
            newErrors.dataInicio = "Data de início é obrigatória";
        }
        if (formData.taxaJurosMensal < 0) {
            newErrors.taxaJurosMensal = "Taxa não pode ser negativa";
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const resetForm = () => {
        setFormData(INITIAL_FORM_DATA);
        setErrors({});
    };

    const handleOpenChange = (isOpen: boolean) => {
        setOpen(isOpen);
        if (!isOpen) {
            resetForm();
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!validateForm()) {
            toast.errorCustom("Formulário inválido", "Verifique os campos destacados.");
            return;
        }

        setLoading(true);
        try {
            await contratoService.cadastrar(formData);
            toast.success("Contrato criado!", "O contrato foi registrado com sucesso.");
            handleOpenChange(false);
            onSucesso();
        } catch (error) {
            // ✅ Tratamento automático de erro do backend
            toast.error(error);
        } finally {
            setLoading(false);
        }
    };

    const updateField = <K extends keyof ContratoRequest>(key: K, value: ContratoRequest[K]) => {
        setFormData(prev => ({ ...prev, [key]: value }));
        if (errors[key]) {
            setErrors(prev => ({ ...prev, [key]: undefined }));
        }
    };

    const selectedImovel = imoveis.find(i => i.id === formData.imovelId);

    const inputStyles = "bg-[#0a0a0b] border-border/60 text-white placeholder:text-muted-foreground";
    const inputErrorStyles = "border-red-400 focus:border-red-400";

    return (
        <Dialog open={open} onOpenChange={handleOpenChange}>
            <DialogTrigger asChild>
                <Button className="gap-2 font-semibold shadow-lg shadow-primary/20 bg-white text-black hover:bg-gray-200">
                    <Plus className="h-4 w-4" />
                    Novo Contrato
                </Button>
            </DialogTrigger>

            <DialogContent className="sm:max-w-[560px] bg-[#121214] border-border/50 text-white max-h-[90vh] overflow-y-auto">
                <DialogHeader>
                    <DialogTitle className="text-xl font-bold flex items-center gap-2">
                        <FileText className="h-5 w-5 text-primary" />
                        Novo Contrato de Locação
                    </DialogTitle>
                    <DialogDescription className="text-muted-foreground">
                        Preencha os dados para criar um contrato.
                    </DialogDescription>
                </DialogHeader>

                <form onSubmit={handleSubmit} className="space-y-5 pt-4">
                    {/* Alerta se não há imóveis */}
                    {!loadingImoveis && imoveis.length === 0 && (
                        <div className="flex items-center gap-2 p-3 rounded-lg border border-yellow-600/50 bg-yellow-600/10 text-yellow-200 text-sm">
                            <AlertCircle className="h-4 w-4 shrink-0" />
                            <span>Não há imóveis disponíveis. Cadastre um imóvel primeiro.</span>
                        </div>
                    )}

                    {/* SEÇÃO: IMÓVEL */}
                    <div className="space-y-4">
                        <div className="flex items-center gap-2 text-sm font-medium text-muted-foreground">
                            <Building2 className="h-4 w-4" />
                            Imóvel
                        </div>

                        <div className="space-y-2">
                            <Label>Selecionar Imóvel <span className="text-red-400">*</span></Label>
                            <Select
                                value={formData.imovelId ? formData.imovelId.toString() : ""}
                                onValueChange={handleImovelChange}
                                disabled={loadingImoveis || imoveis.length === 0}
                            >
                                <SelectTrigger className={cn(inputStyles, errors.imovelId && inputErrorStyles)}>
                                    <SelectValue placeholder={loadingImoveis ? "Carregando..." : "Selecione um imóvel"} />
                                </SelectTrigger>
                                <SelectContent className="bg-[#1a1a1d] border-border">
                                    {imoveis.map(imovel => (
                                        <SelectItem key={imovel.id} value={imovel.id.toString()}>
                                            #{imovel.id} - {imovel.endereco} ({imovel.metrosQuadrados}m²)
                                        </SelectItem>
                                    ))}
                                </SelectContent>
                            </Select>
                            {errors.imovelId && <p className="text-xs text-red-400">{errors.imovelId}</p>}
                        </div>

                        {selectedImovel && (
                            <div className="p-3 rounded-lg bg-primary/10 border border-primary/20 text-sm space-y-1">
                                <div className="flex justify-between">
                                    <span className="text-muted-foreground">Endereço:</span>
                                    <span className="font-medium">{selectedImovel.endereco}</span>
                                </div>
                                <div className="flex justify-between">
                                    <span className="text-muted-foreground">Valor sugerido:</span>
                                    <span className="font-medium text-green-400">
                                        R$ {selectedImovel.valor.toLocaleString("pt-BR")}
                                    </span>
                                </div>
                            </div>
                        )}
                    </div>

                    <Separator className="bg-border/40" />

                    {/* SEÇÃO: PARTES */}
                    <div className="space-y-4">
                        <div className="flex items-center gap-2 text-sm font-medium text-muted-foreground">
                            <User className="h-4 w-4" />
                            Partes Envolvidas
                        </div>

                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                            <div className="space-y-2">
                                <Label>Proprietário <span className="text-red-400">*</span></Label>
                                <Input
                                    placeholder="Nome completo"
                                    className={cn(inputStyles, errors.nomeDono && inputErrorStyles)}
                                    value={formData.nomeDono}
                                    onChange={(e) => updateField("nomeDono", e.target.value)}
                                />
                                {errors.nomeDono && <p className="text-xs text-red-400">{errors.nomeDono}</p>}
                            </div>

                            <div className="space-y-2">
                                <Label>Inquilino <span className="text-red-400">*</span></Label>
                                <Input
                                    placeholder="Nome completo"
                                    className={cn(inputStyles, errors.nomeInquilino && inputErrorStyles)}
                                    value={formData.nomeInquilino}
                                    onChange={(e) => updateField("nomeInquilino", e.target.value)}
                                />
                                {errors.nomeInquilino && <p className="text-xs text-red-400">{errors.nomeInquilino}</p>}
                            </div>
                        </div>
                    </div>

                    <Separator className="bg-border/40" />

                    {/* SEÇÃO: FINANCEIRO */}
                    <div className="space-y-4">
                        <div className="flex items-center gap-2 text-sm font-medium text-muted-foreground">
                            <DollarSign className="h-4 w-4" />
                            Condições Financeiras
                        </div>

                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                            <div className="space-y-2">
                                <Label>Valor Acordado <span className="text-red-400">*</span></Label>
                                <div className="relative">
                                    <span className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground text-sm">R$</span>
                                    <Input
                                        type="number"
                                        step="0.01"
                                        min="0"
                                        placeholder="0,00"
                                        className={cn("pl-9", inputStyles, errors.valorAcordado && inputErrorStyles)}
                                        value={formData.valorAcordado || ""}
                                        onChange={(e) => updateField("valorAcordado", Number(e.target.value))}
                                    />
                                </div>
                                {errors.valorAcordado && <p className="text-xs text-red-400">{errors.valorAcordado}</p>}
                            </div>

                            <div className="space-y-2">
                                <Label className="flex items-center gap-1">
                                    <Percent className="h-3 w-3" />
                                    Taxa Juros Mensal
                                </Label>
                                <div className="relative">
                                    <Input
                                        type="number"
                                        step="0.01"
                                        min="0"
                                        placeholder="0"
                                        className={cn("pr-8", inputStyles)}
                                        value={formData.taxaJurosMensal || ""}
                                        onChange={(e) => updateField("taxaJurosMensal", Number(e.target.value))}
                                    />
                                    <span className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground text-sm">%</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <Separator className="bg-border/40" />

                    {/* SEÇÃO: DETALHES */}
                    <div className="space-y-4">
                        <div className="flex items-center gap-2 text-sm font-medium text-muted-foreground">
                            <Calendar className="h-4 w-4" />
                            Detalhes do Contrato
                        </div>

                        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                            <div className="space-y-2">
                                <Label>Data de Início <span className="text-red-400">*</span></Label>
                                <Input
                                    type="date"
                                    className={cn(inputStyles, errors.dataInicio && inputErrorStyles)}
                                    value={formData.dataInicio}
                                    onChange={(e) => updateField("dataInicio", e.target.value)}
                                />
                                {errors.dataInicio && <p className="text-xs text-red-400">{errors.dataInicio}</p>}
                            </div>

                            <div className="space-y-2">
                                <Label>Tipo de Contrato</Label>
                                <Select
                                    value={formData.tipo}
                                    onValueChange={(v: TipoContrato) => updateField("tipo", v)}
                                >
                                    <SelectTrigger className={inputStyles}>
                                        <SelectValue />
                                    </SelectTrigger>
                                    <SelectContent className="bg-[#1a1a1d] border-border">
                                        <SelectItem value="ALUGUEL">Aluguel</SelectItem>
                                        <SelectItem value="VENDA">Venda</SelectItem>
                                    </SelectContent>
                                </Select>
                            </div>
                        </div>

                        <div className="flex items-center justify-between p-4 rounded-lg bg-muted/20 border border-border/40">
                            <div className="space-y-0.5">
                                <Label className="text-base font-medium">Renovação Automática</Label>
                                <p className="text-sm text-muted-foreground">Permitir renovação ao final</p>
                            </div>
                            <Switch
                                checked={formData.podeRenovar}
                                onCheckedChange={(checked) => updateField("podeRenovar", checked)}
                            />
                        </div>
                    </div>

                    <DialogFooter className="pt-4 gap-3">
                        <Button
                            type="button"
                            variant="ghost"
                            onClick={() => handleOpenChange(false)}
                            disabled={loading}
                        >
                            Cancelar
                        </Button>
                        <Button
                            type="submit"
                            disabled={loading || imoveis.length === 0}
                            className="gap-2 bg-white text-black hover:bg-gray-200"
                        >
                            {loading ? (
                                <><Loader2 className="h-4 w-4 animate-spin" /> Salvando...</>
                            ) : (
                                <><FileText className="h-4 w-4" /> Criar Contrato</>
                            )}
                        </Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
}
