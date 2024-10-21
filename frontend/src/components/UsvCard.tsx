import {Usv} from "../types/usv.ts";
import {useNavigate} from "react-router-dom";
import {FaPlus} from "react-icons/fa";
import './UsvCard.css'

type UsvCardProps = {
    usv: Usv,
    monitoring: boolean
}

export default function UsvCard(props: Readonly<UsvCardProps>){
    const navigate = useNavigate()
    return(
        <div onClick={() => navigate("/usvdetails/" + props.usv?.id)}>
            {props.usv?.id !== "new"
              ? <>
                <h3>{props.usv?.name}</h3>
                <p>{props.usv?.address}</p>
                </>
                : // plus-button for adding
                 <h2><FaPlus/></h2>
            }
        </div>
    )
}