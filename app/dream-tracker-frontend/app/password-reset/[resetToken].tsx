// app/password-reset/[resetToken].tsx

'use client';

import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import axios from 'axios';
import { useRouter } from 'next/router';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { toast } from "@/components/ui/use-toast";

import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form";

const FormSchema = z.object({
    password: z.string().min(6, { message: "Password must be at least 6 characters." }),
    passwordConfirmation: z.string().min(6, { message: "Password must be at least 6 characters." }),
}).refine(data => data.password === data.passwordConfirmation, {
    message: "Passwords do not match",
    path: ["passwordConfirmation"],
});

export default function PasswordResetForm() {
    const router = useRouter();
    const { resetToken } = router.query;

    const form = useForm<z.infer<typeof FormSchema>>({
        resolver: zodResolver(FormSchema),
        defaultValues: {
            password: "",
            passwordConfirmation: "",
        },
    });

    // Funkcja obsługująca wysyłanie formularza
    async function onSubmit(data: z.infer<typeof FormSchema>) {
        try {
            // Wysyłanie żądania do endpointu
            const response = await axios.put('http://localhost:8080/auth/reset-password', {
                password: data.password,
                passwordConfirmation: data.passwordConfirmation,
                resetToken: resetToken, // Przekazanie resetToken z URL
            });

            // Obsługa odpowiedzi serwera
            if (response.status === 200 && response.data === true) {
                toast({
                    title: "Password Reset Successful",
                    description: "Your password has been successfully reset.",
                });
                router.push('/login'); // Przekierowanie na stronę logowania
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
            <div
                aria-hidden="true"
                className="absolute inset-x-0 -top-40 -z-10 transform-gpu overflow-hidden blur-3xl sm:-top-80"
            >
                <div
                    style={{
                        clipPath:
                            'polygon(74.1% 44.1%, 100% 61.6%, 97.5% 26.9%, 85.5% 0.1%, 80.7% 2%, 72.5% 32.5%, 60.2% 62.4%, 52.4% 68.1%, 47.5% 58.3%, 45.2% 34.5%, 27.5% 76.7%, 0.1% 64.9%, 17.9% 100%, 27.6% 76.8%, 76.1% 97.7%, 74.1% 44.1%)',
                    }}
                    className="relative left-[calc(50%-11rem)] aspect-[1155/678] w-[36.125rem] -translate-x-1/2 rotate-[30deg] bg-gradient-to-tr from-[#1f1f3a] to-[#2d2d6c] opacity-30 sm:left-[calc(50%-30rem)] sm:w-[72.1875rem]'
                />
            </div>
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
            <div
                aria-hidden="true"
                className="absolute inset-x-0 top-[calc(100%-13rem)] -z-10 transform-gpu overflow-hidden blur-3xl sm:top-[calc(100%-30rem)]"
                >
                <div
                    style={{
                        clipPath:
                            'polygon(74.1% 44.1%, 100% 61.6%, 97.5% 26.9%, 85.5% 0.1%, 80.7% 2%, 72.5% 32.5%, 60.2% 62.4%, 52.4% 68.1%, 47.5% 58.3%, 45.2% 34.5%, 27.5% 76.7%, 0.1% 64.9%, 17.9% 100%, 27.6% 76.8%, 76.1% 97.7%, 74.1% 44.1%)',
                    }}
                    className="relative left-[calc(50%+3rem)] aspect-[1155/678] w-[36.125rem] -translate-x-1/2 bg-gradient-to-tr from-[#1f1f3a] to-[#2d2d6c] opacity-30 sm:left-[calc(50%+36rem)] sm:w-[72.1875rem]"
                />
            </div>
        </div>
    );
}
