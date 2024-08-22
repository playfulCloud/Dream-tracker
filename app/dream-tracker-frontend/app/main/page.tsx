import BarNav from "@/app/nav/page";
import MainPanel from "@/app/main/main";
import {SiteFooter} from "@/app/nav/footer";


export default function MainPage(){

    return (
        <div>
            <BarNav/>
            <MainPanel/>
            <SiteFooter/>
        </div>
    );

}