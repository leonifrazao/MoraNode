// src/components/ui/error-toast.tsx
import { AlertCircle, XCircle, AlertTriangle, Info } from "lucide-react";

interface ErrorToastProps {
    title: string;
    message: string;
    status?: number;
}

export function getErrorIcon(status?: number) {
    if (!status) return AlertCircle;
    if (status >= 500) return XCircle;
    if (status === 409) return AlertTriangle;
    if (status === 404) return Info;
    return AlertCircle;
}

export function ErrorToast({ title, message, status }: ErrorToastProps) {
    const Icon = getErrorIcon(status);

    return (
        <div className="flex gap-3">
            <Icon className="h-5 w-5 text-red-400 shrink-0 mt-0.5" />
            <div className="space-y-1">
                <p className="font-semibold text-sm">{title}</p>
                <p className="text-sm text-muted-foreground">{message}</p>
            </div>
        </div>
    );
}