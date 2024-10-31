import UpsList from "../components/UpsList.tsx";
import { Server } from "../types/server.ts";
import {Ups} from "../types/ups.ts";

type AllUpsesProps = {
    upses: Ups[],
    servers: Server[]
    monitoring: boolean
}

export default function AllUpsesPage(props:Readonly<AllUpsesProps>){
    return(
        <>
            {props.monitoring ? <h3> Monitoring mode </h3> : <h3> no Monitoring</h3>}
            <h1>List of UPSes</h1>
            <UpsList upses={props.upses} servers={props.servers} monitoring={props.monitoring}/>
        </>
    )
}
