"use client";
import * as React from "react";
import { Button } from "@/components/ui/button";
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

export function StatsCard({ cardTitle, cardDescription, cardContent }) {
    return (
        <Card className="w-[350px]">
            <CardHeader>
                <CardTitle>{cardTitle}</CardTitle>
                <CardDescription>{cardDescription}</CardDescription>
            </CardHeader>
            <CardContent className="text-xl">
                {cardContent}
            </CardContent>
        </Card>
    );
}

export default StatsCard;
