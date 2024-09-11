import HabitTracker from "@/app/habitTracker/habitTracker";
import BarNav from "@/app/nav/page";
import {SiteFooter} from "@/app/nav/footer";


export default function HabitPage(){
    return (
        <div className="flex flex-col min-h-screen">
            <BarNav/>
            <div className="flex-grow">
                <HabitTracker/>
            </div>
            <SiteFooter/>
        </div>
    );
};