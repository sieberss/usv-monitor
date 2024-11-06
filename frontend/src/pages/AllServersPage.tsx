import {Ups} from "../types/ups.ts";
import {Server} from "../types/server.ts";
import ServerList from "../components/ServerList.tsx";
import { Credentials } from "../types/credentials.ts";

type OverviewProps = {
    setMenuItem: (item:string) => void,
    servers: Server[],
    upses: Ups[],
    credentialsList: Credentials[],
    monitoring: boolean
}

export default function AllUpsesPage(props:Readonly<OverviewProps>){
    props.setMenuItem("server")
     return(
            <ServerList servers={props.servers} upses={props.upses} credentialsList={props.credentialsList} monitoring={props.monitoring}/>
     )
}
