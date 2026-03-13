// components/ui/stat-card.tsx
import { Card, CardContent } from "@/components/ui/card";
import { LucideIcon } from "lucide-react";

interface StatCardProps {
    title: string;
    value: string | number;
    description?: string;
    icon: LucideIcon;
    trend?: "up" | "down" | "neutral";
    trendValue?: string;
    variant?: "default" | "success" | "warning" | "danger";
}

const variantStyles = {
    default: "text-primary bg-primary/10",
    success: "text-green-500 bg-green-500/10",
    warning: "text-yellow-500 bg-yellow-500/10",
    danger: "text-red-500 bg-red-500/10",
};

export function StatCard({
    title,
    value,
    description,
    icon: Icon,
    variant = "default"
}: StatCardProps) {
    return (
        <Card className="border-border/50 bg-card/30 backdrop-blur-sm">
            <CardContent className="p-6">
                <div className="flex items-center justify-between">
                    <div className="space-y-1">
                        <p className="text-sm font-medium text-muted-foreground">{title}</p>
                        <p className="text-2xl font-bold text-white">{value}</p>
                        {description && (
                            <p className="text-xs text-muted-foreground">{description}</p>
                        )}
                    </div>
                    <div className={`p-3 rounded-full ${variantStyles[variant]}`}>
                        <Icon className="h-5 w-5" />
                    </div>
                </div>
            </CardContent>
        </Card>
    );
}