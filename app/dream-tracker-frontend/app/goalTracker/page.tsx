import BarNav from "@/app/nav/page";
import GoalTable from "@/app/goalTracker/goalTracker";
import {SiteFooter} from "@/app/nav/footer";


export default function GoalPage(){
    return (
        <div className="flex flex-col min-h-screen">
            <BarNav/>
            <div className="flex-grow">
                <GoalTable/>
            </div>
            <SiteFooter/>
        </div>
    )
}