import { Credentials } from "../types/credentials.ts";
import { Server } from "../types/server.ts";
import {Ups} from "../types/ups.ts";
import ServerCard from "./ServerCard.tsx";
import './ServerList.css'
import {Status} from "../types/status.ts";

type ServerListProps = {
    servers: Server[],
    upses: Ups[],
    credentialsList: Credentials[]
    monitoring: boolean,
    getServerStatus: (id: string) => Status | undefined
}

export default function ServerList(props:Readonly<ServerListProps>){


    return(
        <ul className={"serverlist"}>
            {props.servers.map(server =>
                <ServerCard key={server.id} server={server} upses={props.upses} credentialsList={props.credentialsList}
                                monitoring={props.monitoring} getServerStatus={props.getServerStatus}/>
            )}
            {props.monitoring // don't show add button in monitoring mode
                || <ServerCard server={{id:"new", name:"", address:"", credentials: {id:"", user:"", password:"", global:true}, upsId:"", shutdownTime:180}}
                               upses={props.upses} credentialsList={props.credentialsList} monitoring={props.monitoring} getServerStatus={props.getServerStatus}/>}
        </ul>
    )
}