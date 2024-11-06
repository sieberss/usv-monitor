import {Server} from "../types/server.ts";
import {Ups} from "../types/ups.ts";
import UpsCard from "./UpsCard.tsx";
import './UpsList.css'

type UpsListProps = {
    upses: Ups[],
    servers: Server[],
    monitoring: boolean
}

export default function UpsList(props: Readonly<UpsListProps>) {
    const serversToUps: Map<Ups, Server[]> = new Map()
    props.upses.forEach(ups => serversToUps.set(ups, props.servers.filter((s => s.upsId === ups.id))))
    return (
        <ul className={"upslist"}>
            {props.upses.map((ups) =>
                <li className={"upscard"}>
                    <UpsCard ups={ups}
                             servers={serversToUps.get(ups)}
                             monitoring={props.monitoring} key={ups.id}/>
                </li>
            )}
            {props.monitoring  // add a card for adding a new UPS only when not monitoring
                ||
                <li className={"upscard"}>
                    <UpsCard ups={{id: "new", name: "", community: "", address: ""}} servers={[]} monitoring={false}/>
                </li>}
        </ul>
    )
}