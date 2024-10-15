import {Usv} from "../types/usv.ts";

type UsvCardProps = {
    usv: Usv,
    monitoring: boolean
}

export default function UsvCard(props: Readonly<UsvCardProps>){
    return(
        <>
            <h3>{props.usv.name}</h3>
            <p>{props.usv.address}</p>
        </>
    )
}