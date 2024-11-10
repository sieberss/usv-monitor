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
    getServerStatus: (id: string) => Status | undefined
}

export default function ServerCard(props: Readonly<ServerCardProps>) {
    const status: Status | undefined = props.getServerStatus(props.server.id)

    function getClassName(): string {
        if (!props.monitoring)
            return "server-card"
        switch (status?.state) {
            case "POWER_OFF":
                return "server-card-poweroff";
            case "POWER_OFF_LIMIT":
            case "SHUTDOWN":
                return "server-card-shutdown";
            default:
                return "server-card";
        }
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