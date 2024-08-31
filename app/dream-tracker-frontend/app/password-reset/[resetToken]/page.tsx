'use client'
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { z } from "zod"
import axios from 'axios'
import { useParams, useRouter } from 'next/navigation'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { toast } from "@/components/ui/use-toast"

import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form"

const FormSchema = z.object({
    password: z.string().min(6, { message: "Password must be at least 6 characters." }),
    passwordConfirmation: z.string().min(6, { message: "Password must be at least 6 characters." }),
}).refine(data => data.password === data.passwordConfirmation, {
    message: "Passwords do not match",
    path: ["passwordConfirmation"],
});

function PasswordResetFormComponent() {
    const params = useParams();
    const router = useRouter();
    const resetToken = params.resetToken;

    const form = useForm<z.infer<typeof FormSchema>>({
        resolver: zodResolver(FormSchema),
        defaultValues: {
            password: "",
            passwordConfirmation: "",
        },
    });

    async function onSubmit(data: z.infer<typeof FormSchema>) {
        try {
            const response = await axios.put('http://localhost:8080/v1/auth/reset-password', {
                password: data.password,
                passwordConfirmation: data.passwordConfirmation,
                resetToken: resetToken,
            });

            if (response.status === 200 && response.data === true) {
                toast({
                    title: "Password Reset Successful",
                    description: "Your password has been successfully reset.",
                });
                router.push('/login');
            } else {
                toast({
                    title: "Password Reset Failed",
                    description: "Failed to reset your password. Please try again.",
                });
            }
        } catch (error) {
            toast({
                title: "Request Failed",
                description: error.response?.data?.message || String(error),
            });
        }
    }

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-900 relative isolate px-6 lg:px-8">
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6 w-full max-w-md mx-auto bg-white p-8 rounded-lg shadow-md z-10">
                    <FormField
                        control={form.control}
                        name="password"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>New Password</FormLabel>
                                <FormControl>
                                    <Input type="password" placeholder="Enter new password" {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="passwordConfirmation"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Confirm New Password</FormLabel>
                                <FormControl>
                                    <Input type="password" placeholder="Confirm new password" {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <Button type="submit" className="w-full">Reset Password</Button>
                </form>
            </Form>
        </div>
    );
}

const PasswordReset = () => {
    return <PasswordResetFormComponent />
}

export default PasswordReset;
