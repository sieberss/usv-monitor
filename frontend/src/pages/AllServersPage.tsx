import {Ups} from "../types/ups.ts";
import {Server} from "../types/server.ts";
import ServerList from "../components/ServerList.tsx";
import { Credentials } from "../types/credentials.ts";

type OverviewProps = {
    servers: Server[],
    upses: Ups[],
    credentialsList: Credentials[],
    monitoring: boolean,
    getServerClassName: (server: Server) => string
}

export default function AllServersPage(props:Readonly<OverviewProps>){
     return(
            <ServerList servers={props.servers} upses={props.upses} credentialsList={props.credentialsList}
                        monitoring={props.monitoring} getServerClassName={props.getServerClassName}/>
     )
}
