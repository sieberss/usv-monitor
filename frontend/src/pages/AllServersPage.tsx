import {Ups} from "../types/ups.ts";
import {Server} from "../types/server.ts";
import ServerList from "../components/ServerList.tsx";
import { Credentials } from "../types/credentials.ts";
import {Status} from "../types/status.ts";

type OverviewProps = {
    servers: Server[],
    upses: Ups[],
    credentialsList: Credentials[],
    monitoring: boolean,
    getServerStatus: (id: string) => Status | undefined
}

export default function AllServersPage(props:Readonly<OverviewProps>){
     return(
            <ServerList servers={props.servers} upses={props.upses} credentialsList={props.credentialsList}
                        monitoring={props.monitoring} getServerStatus={props.getServerStatus}/>
     )
}
