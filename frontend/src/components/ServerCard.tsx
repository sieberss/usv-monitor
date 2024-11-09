import {Ups} from "../types/ups.ts";
import {FaPlus} from "react-icons/fa";
import {Credentials} from "../types/credentials.ts";
import {Server} from "../types/server.ts";
import {Status} from "../types/status.ts";

type ServerCardProps = {
    server: Server,
    upses: Ups[],
    credentialsList: Credentials[],
    monitoring: boolean,
    getUpsStatus: (id: string) => Status|undefined
}

export default function ServerCard(props: Readonly<ServerCardProps>) {
    const upsStatus: Status | undefined = props.getUpsStatus(props.server.upsId)
    const server: Server = props.server

    function getClassName(): string {
        if (!props.monitoring || upsStatus?.state === "POWER_ON" || server.id === "new")
            return "server-card"
        else if (upsStatus?.remaining && upsStatus.remaining > server.shutdownTime)
            return "server-card-poweroff"
        else return "server-card-shutdown"
    }

    return (
        <li className={getClassName()}>
            <a href={"/server/" + props.server?.id}>
                {props.server?.id !== "new"
                    ? <>
                        <h3>{props.server?.name}</h3>
                        <p>{props.server?.address}</p>
                    </>
                    : // plus-button for adding
                    <h2><FaPlus/></h2>
                }
            </a>
        </li>
    )
}