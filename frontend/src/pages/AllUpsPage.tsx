import UpsList from "../components/UpsList.tsx";
import {Ups} from "../types/ups.ts";

type OverviewProps = {
    upss: Ups[],
    monitoring: boolean
}

export default function AllUpsPage(props:Readonly<OverviewProps>){
    return(
        <>
            {props.monitoring ? <h3> Monitoring mode </h3> : <h3> no Monitoring</h3>}
            <h1>List of UPSs</h1>
            <UpsList upss={props.upss} monitoring={props.monitoring}/>
        </>
    )
}
