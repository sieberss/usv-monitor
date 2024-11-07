import { Credentials } from "../types/credentials.ts";
import { Server } from "../types/server.ts";
import {Ups} from "../types/ups.ts";
import ServerCard from "./ServerCard.tsx";
import './ServerList.css'

type ServerListProps = {
    servers: Server[],
    upses: Ups[],
    credentialsList: Credentials[]
    monitoring: boolean
}

export default function ServerList(props:Readonly<ServerListProps>){
    function getClassName(server: Server): string {
        return "servercard"
    }

    return(
        <ul className={"serverlist"}>
            {props.servers.map(server =>
                <li className={getClassName(server)} key={server.id}>
                    <ServerCard server={server} upses={props.upses} credentialsList={props.credentialsList} monitoring={props.monitoring}/>
                </li>
            )}

            {props.monitoring  // add a card for adding a new UPS only when not monitoring
                ||
                <li className={"servercard"}>
                    <ServerCard server={{id:"new", name:"", address:"", credentials: {id:"", user:"", password:"", global:true}, upsId:"", shutdownTime:180}}
                               upses={props.upses} credentialsList={props.credentialsList} monitoring={false}/>
                </li>
            }

        </ul>
    )
}