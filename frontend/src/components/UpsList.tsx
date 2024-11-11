import {Server} from "../types/server.ts";
import {Ups} from "../types/ups.ts";
import UpsCard from "./UpsCard.tsx";
import './UpsList.css'
import {Status} from "../types/status.ts";

type UpsListProps = {
    upses: Ups[],
    servers: Server[],
    monitoring: boolean,
    getUpsStatus: (id: string) => Status | undefined
}

export default function UpsList(props: Readonly<UpsListProps>) {
    const serversToUps: Map<Ups, Server[]> = new Map()
    props.upses.forEach(ups => serversToUps.set(ups, props.servers.filter((s => s.upsId === ups.id))))

    return (
        <ul className={"upslist"}>
            {props.upses.map((ups) =>
                <UpsCard key={ups.id} ups={ups} servers={serversToUps.get(ups)}
                         monitoring={props.monitoring} getUpsStatus={props.getUpsStatus}/>
            )}
            {props.monitoring // don't show add button in monitoring mode
                || <UpsCard ups={{id: "new", name: "", community: "", address: ""}} servers={[]}
                     monitoring={props.monitoring} getUpsStatus={props.getUpsStatus}/>}
        </ul>
    )
}