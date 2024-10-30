import {Ups} from "../types/ups.ts";
import {Server} from "../types/server.ts";
import ServerList from "../components/ServerList.tsx";
import { Credentials } from "../types/credentials.ts";

type OverviewProps = {
    servers: Server[],
    upses: Ups[],
    credentialsList: Credentials[],
    monitoring: boolean
}

export default function AllUpsesPage(props:Readonly<OverviewProps>){
     return(
        <>
            {props.monitoring ? <h3> Monitoring mode </h3> : <h3> no Monitoring</h3>}
            <h1>List of Servers</h1>
            <ServerList servers={props.servers} upses={props.upses} credentialsList={props.credentialsList} monitoring={props.monitoring}/>
        </>
    )
}
