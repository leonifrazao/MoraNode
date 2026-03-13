// src/hooks/use-toast-feedback.ts
import { useToast } from "./use-toast"
import { parseError } from "@/lib/error-utils"

export function useToastFeedback() {
    const { toast } = useToast()

    return {
        success: (title: string, description?: string) => {
            toast({
                title,
                description,
                className: "bg-green-900/90 border-green-700 text-white",
            })
        },

        error: (error: unknown) => {
            const { title, message } = parseError(error)
            toast({
                title,
                description: message,
                variant: "destructive",
            })
        },

        errorCustom: (title: string, description?: string) => {
            toast({
                title,
                description,
                variant: "destructive",
            })
        },

        info: (title: string, description?: string) => {
            toast({ title, description })
        },

        warning: (title: string, description?: string) => {
            toast({
                title,
                description,
                className: "bg-yellow-900/90 border-yellow-700 text-white",
            })
        },
    }
}