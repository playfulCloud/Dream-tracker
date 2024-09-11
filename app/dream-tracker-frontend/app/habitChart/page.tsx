import BarNav from "@/app/nav/page";
import MainPanel from "@/app/main/main";
import {SiteFooter} from "@/app/nav/footer";
import HabitCharts from "@/app/habitChart/habitCharts";


export default function HabitChartPage(){

    return (
        <div className="flex flex-col min-h-screen">
            <BarNav/>
            <div className="flex-grow">
                <HabitCharts/>
            </div>
            <SiteFooter/>
        </div>
    );

}