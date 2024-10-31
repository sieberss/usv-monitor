import { Server } from "../types/server.ts";
import {Ups} from "../types/ups.ts";
import UpsCard from "./UpsCard.tsx";

type UpsListProps = {
    upses: Ups[],
    servers: Server[],
    monitoring: boolean
}

export default function UpsList(props:Readonly<UpsListProps>){
    const serversToUps:Map<Ups, Server[]> = new Map()
    props.upses.forEach(ups => serversToUps.set(ups, props.servers.filter((s => s.upsId === ups.id))))
    return(
        <>
            {props.upses.map((ups) =>
                <UpsCard ups={ups}
                         servers={serversToUps.get(ups)}
                         monitoring={props.monitoring} key={ups.id}/>)}

            {props.monitoring  // add a card for adding a new UPS only when not monitoring
                || <UpsCard ups={{id:"new", name:"", community:"", address:""}} servers={[]} monitoring={false}/>
            }

        </>
    )
}