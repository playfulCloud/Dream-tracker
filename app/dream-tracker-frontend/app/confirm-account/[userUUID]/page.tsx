'use client'
import axios from 'axios';
import { useParams, useRouter } from 'next/navigation';
import { Button } from "@/components/ui/button";
import { toast } from "@/components/ui/use-toast";

import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useForm } from "react-hook-form";

const FormSchema = z.object({
});

function ConfirmAccountFormComponent() {
    const params = useParams();
    const router = useRouter();
    const userUUID = params.userUUID;

    async function onSubmit() {
        event.preventDefault();
        console.log(userUUID)
        try {
            const response = await axios.put('http://localhost:8080/v1/auth/confirm-password', {
                userUUID: userUUID,
            });
            console.log(response)

            if (response.status === 200) {
                toast({
                    title: "Account Confirmed",
                    description: "Your account has been successfully confirmed.",
                });
                router.push('/login');
            } else {
                toast({
                    title: "Confirmation Failed",
                    description: "Failed to confirm your account. Please try again.",
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
            <Form>
                <form onSubmit={onSubmit} className="space-y-6 w-full max-w-md mx-auto bg-white p-8 rounded-lg shadow-md z-10">
                    <Button type="submit" className="w-full">Confirm Account</Button>
                </form>
            </Form>
        </div>
    );
}

const ConfirmAccountPage = () => {
    return <ConfirmAccountFormComponent />;
};

export default ConfirmAccountPage;
