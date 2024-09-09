import Stats from "@/app/stats/stats";
import BarNav from "@/app/nav/page";
import {SiteFooter} from "@/app/nav/footer";


export default function StatsPage(){
    return(
        <div className="flex flex-col min-h-screen">
            <BarNav/>
            <div className="flex-grow">
                <Stats/>
            </div>
            <SiteFooter/>
        </div>
    )
}