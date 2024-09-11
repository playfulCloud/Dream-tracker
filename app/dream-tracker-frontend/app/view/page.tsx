import BarNav from "@/app/nav/page";
import ViewManager from "@/app/view/viewPanel";
import {SiteFooter} from "@/app/nav/footer";


export default function ViewPage(){
    return (
    <div className="flex flex-col min-h-screen">
        <BarNav/>
        <div className="flex-grow">
            <ViewManager></ViewManager>
        </div>
        <SiteFooter/>
    </div>
)
}