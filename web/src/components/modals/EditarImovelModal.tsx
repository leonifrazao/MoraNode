// components/modals/EditarImovelModal.tsx
import { useState, useEffect } from "react";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogFooter,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import { Loader2, MapPin, Maximize, DollarSign, AlertCircle, Pencil } from "lucide-react";
import { imovelService } from "@/services/api";
import { ImovelRequest, ImovelResponse } from "@/types";
import { cn } from "@/lib/utils";
import { parseError } from "@/lib/error-utils";

interface Props {
    imovel: ImovelResponse | null;
    open: boolean;
    onOpenChange: (open: boolean) => void;
    onSucesso: () => void;
}

export function EditarImovelModal({ imovel, open, onOpenChange, onSucesso }: Props) {
    const [loading, setLoading] = useState(false);
    const [formData, setFormData] = useState<ImovelRequest>({
        endereco: "",
        valor: 0,
        metrosQuadrados: 0,
        disponivel: true,
    });
    const [errors, setErrors] = useState<Partial<Record<keyof ImovelRequest, string>>>({});
    const [serverError, setServerError] = useState<{ title: string; message: string } | null>(null);

    // Preencher o form quando o imóvel mudar
    useEffect(() => {
        if (imovel) {
            setFormData({
                endereco: imovel.endereco || "",
                valor: imovel.valor,
                metrosQuadrados: imovel.metrosQuadrados,
                disponivel: imovel.disponivel,
            });
            setErrors({});
            setServerError(null);
        }
    }, [imovel]);

    const validateForm = (): boolean => {
        const newErrors: Partial<Record<keyof ImovelRequest, string>> = {};

        if (!formData.endereco.trim()) {
            newErrors.endereco = "Endereço é obrigatório";
        }

        if (!formData.valor || formData.valor <= 0) {
            newErrors.valor = "Valor deve ser maior que 0";
        } else if (!Number.isInteger(formData.valor)) {
            newErrors.valor = "Valor deve ser um número inteiro";
        }

        if (!formData.metrosQuadrados || formData.metrosQuadrados <= 0) {
            newErrors.metrosQuadrados = "Área deve ser maior que 0";
        } else if (!Number.isInteger(formData.metrosQuadrados)) {
            newErrors.metrosQuadrados = "Área deve ser um número inteiro";
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleClose = () => {
        if (!loading) {
            onOpenChange(false);
            setErrors({});
            setServerError(null);
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setServerError(null);

        if (!imovel || !validateForm()) {
            return;
        }

        setLoading(true);
        try {
            const payload: ImovelRequest = {
                endereco: formData.endereco.trim(),
                valor: Math.floor(formData.valor),
                metrosQuadrados: Math.floor(formData.metrosQuadrados),
                disponivel: formData.disponivel,
            };

            await imovelService.editar(imovel.id, payload);
            handleClose();
            onSucesso();
        } catch (error) {
            const { title, message } = parseError(error);
            setServerError({ title, message });
        } finally {
            setLoading(false);
        }
    };

    const updateField = <K extends keyof ImovelRequest>(key: K, value: ImovelRequest[K]) => {
        setFormData(prev => ({ ...prev, [key]: value }));
        if (errors[key]) setErrors(prev => ({ ...prev, [key]: undefined }));
        if (serverError) setServerError(null);
    };

    const handleNumberChange = (key: "valor" | "metrosQuadrados", value: string) => {
        const cleanValue = value.replace(/[^0-9]/g, "");
        const numValue = cleanValue ? parseInt(cleanValue, 10) : 0;
        updateField(key, numValue);
    };

    const inputStyles = "bg-[#0a0a0b] border-border/60 text-white placeholder:text-muted-foreground";
    const inputErrorStyles = "border-red-400";

    return (
        <Dialog open={open} onOpenChange={handleClose}>
            <DialogContent className="sm:max-w-[480px] bg-[#121214] border-border/50 text-white">
                <DialogHeader>
                    <DialogTitle className="text-xl font-bold flex items-center gap-2">
                        <Pencil className="h-5 w-5 text-primary" />
                        Editar Imóvel #{imovel?.id}
                    </DialogTitle>
                    <DialogDescription className="text-muted-foreground">
                        Atualize as informações do imóvel.
                    </DialogDescription>
                </DialogHeader>

                <form onSubmit={handleSubmit} className="space-y-5 pt-4">
                    {/* Erro do Servidor */}
                    {serverError && (
                        <div className="flex items-start gap-3 p-4 rounded-lg border border-red-500/50 bg-red-500/10">
                            <AlertCircle className="h-5 w-5 text-red-500 shrink-0 mt-0.5" />
                            <div>
                                <p className="font-semibold text-red-400">{serverError.title}</p>
                                <p className="text-sm text-red-300/80 mt-1">{serverError.message}</p>
                            </div>
                        </div>
                    )}

                    {/* Endereço */}
                    <div className="space-y-2">
                        <Label className="flex items-center gap-2">
                            <MapPin className="h-4 w-4 text-muted-foreground" />
                            Endereço <span className="text-red-400">*</span>
                        </Label>
                        <Input
                            placeholder="Rua, Número, Bairro, Cidade..."
                            className={cn(inputStyles, errors.endereco && inputErrorStyles)}
                            value={formData.endereco}
                            onChange={(e) => updateField("endereco", e.target.value)}
                        />
                        {errors.endereco && <p className="text-xs text-red-400">{errors.endereco}</p>}
                    </div>

                    {/* Valor e Área */}
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                        <div className="space-y-2">
                            <Label className="flex items-center gap-2">
                                <DollarSign className="h-4 w-4 text-muted-foreground" />
                                Valor Mensal <span className="text-red-400">*</span>
                            </Label>
                            <div className="relative">
                                <span className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground text-sm">R$</span>
                                <Input
                                    type="text"
                                    inputMode="numeric"
                                    placeholder="1500"
                                    className={cn("pl-9", inputStyles, errors.valor && inputErrorStyles)}
                                    value={formData.valor || ""}
                                    onChange={(e) => handleNumberChange("valor", e.target.value)}
                                />
                            </div>
                            {errors.valor && <p className="text-xs text-red-400">{errors.valor}</p>}
                        </div>

                        <div className="space-y-2">
                            <Label className="flex items-center gap-2">
                                <Maximize className="h-4 w-4 text-muted-foreground" />
                                Área <span className="text-red-400">*</span>
                            </Label>
                            <div className="relative">
                                <Input
                                    type="text"
                                    inputMode="numeric"
                                    placeholder="50"
                                    className={cn("pr-10", inputStyles, errors.metrosQuadrados && inputErrorStyles)}
                                    value={formData.metrosQuadrados || ""}
                                    onChange={(e) => handleNumberChange("metrosQuadrados", e.target.value)}
                                />
                                <span className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground text-sm">m²</span>
                            </div>
                            {errors.metrosQuadrados && <p className="text-xs text-red-400">{errors.metrosQuadrados}</p>}
                        </div>
                    </div>

                    {/* Disponibilidade */}
                    <div className="flex items-center justify-between p-4 rounded-lg bg-muted/20 border border-border/40">
                        <div className="space-y-0.5">
                            <Label className="text-base font-medium">Disponível para locação</Label>
                            <p className="text-sm text-muted-foreground">
                                {formData.disponivel ? "Aparecerá como disponível" : "Aparecerá como ocupado"}
                            </p>
                        </div>
                        <Switch
                            checked={formData.disponivel}
                            onCheckedChange={(checked) => updateField("disponivel", checked)}
                        />
                    </div>

                    <DialogFooter className="pt-4 gap-3">
                        <Button type="button" variant="ghost" onClick={handleClose} disabled={loading}>
                            Cancelar
                        </Button>
                        <Button type="submit" disabled={loading} className="gap-2 bg-white text-black hover:bg-gray-200">
                            {loading ? (
                                <><Loader2 className="h-4 w-4 animate-spin" /> Salvando...</>
                            ) : (
                                <><Pencil className="h-4 w-4" /> Salvar Alterações</>
                            )}
                        </Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
}