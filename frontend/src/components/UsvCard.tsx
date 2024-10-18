import {Usv} from "../types/usv.ts";
import {useNavigate} from "react-router-dom";

type UsvCardProps = {
    usv: Usv,
    monitoring: boolean
}

export default function UsvCard(props: Readonly<UsvCardProps>){
    const navigate = useNavigate()
    return(
        <div onClick={() => navigate("/usvdetails/" + props.usv.id)}>
            <h3>{props.usv.name}</h3>
            <p>{props.usv.address}</p>
        </div>
    )
}