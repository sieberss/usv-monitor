import {Ups} from "../types/ups.ts";
import {FaPlus} from "react-icons/fa";
import {Server} from "../types/server.ts";
import {Status} from "../types/status.ts";
import StatusInfo from "./StatusInfo.tsx";

type UpsCardProps = {
    ups: Ups,
    servers: Server[] | undefined,
    monitoring: boolean,
    getUpsStatus: (id: string) => Status|undefined
}

export default function UpsCard(props: Readonly<UpsCardProps>) {
    const ups: Ups = props.ups
    const status: Status|undefined = props.getUpsStatus(ups.id)
    function getClassName (): string {
        return props.monitoring && (status?.state === "POWER_OFF")
            ? "ups-card-poweroff" : "ups-card"
    }

    return (
        <li className={getClassName()}>
            <a href={"/ups/" + props.ups?.id}>
                {props.ups?.id !== "new"
                    ? <>
                        <h3>{props.ups?.name}</h3>
                        <p>{props.ups?.address}</p>
                        {props.monitoring && (ups.id !== "new")
                            && <StatusInfo status={status}/>}
                    </>
                    : // plus-button for adding
                    <h2><FaPlus/></h2>
                }
            </a>
        </li>
    )
}