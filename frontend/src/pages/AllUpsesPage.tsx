import UpsList from "../components/UpsList.tsx";
import {Ups} from "../types/ups.ts";

type OverviewProps = {
    upses: Ups[],
    monitoring: boolean
}

export default function AllUpsesPage(props:Readonly<OverviewProps>){
    return(
        <>
            {props.monitoring ? <h3> Monitoring mode </h3> : <h3> no Monitoring</h3>}
            <h1>List of UPSes</h1>
            <UpsList upses={props.upses} monitoring={props.monitoring}/>
        </>
    )
}
