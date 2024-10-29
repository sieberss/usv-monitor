import { Credentials } from "../types/credentials.ts";
import { Server } from "../types/server.ts";
import {Ups} from "../types/ups.ts";
import ServerCard from "./ServerCard.tsx";

type ServerListProps = {
    servers: Server[],
    upses: Ups[],
    credentialsList: Credentials[]
    monitoring: boolean
}

export default function ServerList(props:Readonly<ServerListProps>){
    return(
        <>
            {props.servers.map(server =>
                <ServerCard server={server} upses={props.upses} credentialsList={props.credentialsList} monitoring={props.monitoring} key={server.id}/>)
            }

            {props.monitoring  // add a card for adding a new UPS only when not monitoring
                || <ServerCard server={{id:"new", name:"", address:"", credentials: {id:"", user:"", password:"", global:true}, upsId:"", shutdownTime:180}}
                               upses={props.upses} credentialsList={props.credentialsList} monitoring={false}/>
            }

        </>
    )
}