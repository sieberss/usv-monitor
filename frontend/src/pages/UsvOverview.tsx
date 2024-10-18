import UsvList from "../components/UsvList.tsx";
import {Usv} from "../types/usv.ts";

type OverviewProps = {
    usvs: Usv[],
    monitoring: boolean
}

export default function UsvOverview(props:Readonly<OverviewProps>){
    return(
        <>
            {props.monitoring ? <h3> Monitoring aktiv </h3> : <h3> kein Monitoring</h3>}
            <h1>Liste der USVen</h1>
            <UsvList usvs={props.usvs} monitoring={props.monitoring}/>
        </>
    )
}
