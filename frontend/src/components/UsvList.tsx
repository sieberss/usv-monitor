import {Usv} from "../types/usv.ts";
import UsvCard from "./UsvCard.tsx";

type UsvListProps = {
    usvs: Usv[],
    monitoring: boolean
}

export default function UsvList(props:Readonly<UsvListProps>){
    return(
        <>
            {props.usvs.map((usv) => <UsvCard usv={usv} monitoring={props.monitoring} key={usv.id}/>)}
        </>
    )
}