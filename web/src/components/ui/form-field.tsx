// components/ui/form-field.tsx
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { cn } from "@/lib/utils";

interface FormFieldProps {
    label: string;
    description?: string;
    error?: string;
    required?: boolean;
    children?: React.ReactNode;
    className?: string;
}

export function FormField({
    label,
    description,
    error,
    required,
    children,
    className
}: FormFieldProps) {
    return (
        <div className={cn("space-y-2", className)}>
            <Label className="text-sm font-medium flex items-center gap-1">
                {label}
                {required && <span className="text-red-400">*</span>}
            </Label>
            {children}
            {description && !error && (
                <p className="text-xs text-muted-foreground">{description}</p>
            )}
            {error && (
                <p className="text-xs text-red-400">{error}</p>
            )}
        </div>
    );
}

interface FormInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
    label: string;
    description?: string;
    error?: string;
}

export function FormInput({
    label,
    description,
    error,
    required,
    className,
    ...props
}: FormInputProps) {
    return (
        <FormField label={label} description={description} error={error} required={required}>
            <Input
                className={cn(
                    "bg-background/50 border-border/50 focus:border-primary transition-colors",
                    error && "border-red-400 focus:border-red-400",
                    className
                )}
                required={required}
                {...props}
            />
        </FormField>
    );
}