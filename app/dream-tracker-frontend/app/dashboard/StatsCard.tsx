"use client";
import * as React from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";

export default function StatsCard({ cardTitle, cardDescription, cardContent, icon }) {
    return (
        <Card className="flex flex-row items-center space-x-4 p-4 shadow-lg">
            <div>{icon}</div> {/* Ikona */}
            <div>
                <CardHeader>
                    <CardTitle>{cardTitle}</CardTitle>
                    <CardDescription>{cardDescription}</CardDescription>
                </CardHeader>
                <CardContent className="text-lg font-semibold">
                    {cardContent}
                </CardContent>
            </div>
        </Card>
    );
}
