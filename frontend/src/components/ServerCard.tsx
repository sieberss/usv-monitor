import {Ups} from "../types/ups.ts";
import {FaPlus} from "react-icons/fa";
import './UpsCard.css'
import {Credentials} from "../types/credentials.ts";
import {Server} from "../types/server.ts";

type ServerCardProps = {
    server: Server,
    upses: Ups[],
    credentialsList: Credentials[],
    monitoring: boolean
}

export default function ServerCard(props: Readonly<ServerCardProps>) {
    return (
        <a href={"/server/" + props.server?.id}>
            <div>
                {props.server?.id !== "new"
                    ? <>
                        <h3>{props.server?.name}</h3>
                        <p>{props.server?.address}</p>
                    </>
                    : // plus-button for adding
                    <h2><FaPlus/></h2>
                }
            </div>
        </a>
    )
}