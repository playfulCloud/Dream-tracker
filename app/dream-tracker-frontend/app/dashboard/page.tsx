import React from "react";
import StatsCard from "@/app/dashboard/StatsCard";
import {
    Card,
} from "@/components/ui/card";
export default function Dashboard() {
    return (
        <div>
            <Card>
                <StatsCard
                    cardTitle="Average Break"
                    cardDescription="Describes the average time between habit completion"
                    cardContent="1.5 hours"
                />
                <StatsCard
                    cardTitle="Streak"
                    cardDescription="Describes the number of periods of doing habit in a row"
                    cardContent="1.5 hours"
                />
                <StatsCard
                    cardTitle="Quantity of habits"
                    cardDescription="Counts number of done and undone habits and calculates trend based on them"
                    cardContent="1.5 hours"
                />
                <StatsCard
                    cardTitle="Depending on day"
                    cardDescription="Describes the average time between habit completion"
                    cardContent="1.5 hours"
                />
            </Card>
        </div>
    );
}
