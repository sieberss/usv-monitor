import {Ups} from "../types/ups.ts";
import {FaPlus} from "react-icons/fa";
import {Server} from "../types/server.ts";

type UpsCardProps = {
    ups: Ups,
    servers: Server[] | undefined,
    monitoring: boolean
}

export default function UpsCard(props: Readonly<UpsCardProps>) {
    return (
        <a href={"/ups/" + props.ups?.id}>
            {props.ups?.id !== "new"
                ? <>
                    <h3>{props.ups?.name}</h3>
                    <p>{props.ups?.address}</p>
                    {/*props.servers?.map(server =>
                            <p className={"server"}> {server.name} ({server.address})</p>
                        )  not sure whether to display here */}
                </>
                : // plus-button for adding
                <h2><FaPlus/></h2>
            }
        </a>
    )
}