import React from "react";
// import { IconComponentNode } from "./IconComponentNode";
// import { MdiDollar } from "./MdiDollar";
// import { MoreVerticalOutline } from "./MoreVerticalOutline";
// import { TrendingUpOutline } from "./TrendingUpOutline";
// import image1 from "./image.png";
// import image from "./image.svg";
import "./style.css";
// import vector from "./vector.svg";

export const PipelineCommerciale = () => {
  return (
    <div className="pipeline-commerciale">
      <div className="total-balance">
        <div className="overlap">
          <TrendingUpOutline className="trending-up-outline" />
          <div className="text-wrapper">CA en cours</div>

          <div className="div">$4,156.45</div>

          <div className="payment-icon">
            <div className="overlap-group">
              <div className="sync-lock">
                {/* <img className="group" alt="Group" src={image1} /> */}
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="overlap-wrapper">
        <div className="overlap-2">
          <TrendingUpOutline className="trending-up-outline-instance" />
          <div className="text-wrapper-2">En attente</div>

          <div className="text-wrapper-3">$4,156.45</div>

          <div className="vector-wrapper">
            {/* <img className="vector" alt="Vector" src={image} /> */}
          </div>
        </div>
      </div>

      <div className="income-section">
        <div className="overlap-2">
          <div className="text-wrapper-4">En preparation</div>

          <div className="text-wrapper-5">$3,146.45</div>

          <div className="dollar-icon">
            <MdiDollar className="mdi-dollar" />
          </div>

          <MoreVerticalOutline className="more-vertical" />
        </div>
      </div>

      <div className="expeses-section">
        <div className="overlap-3">
          <div className="img-wrapper">
            {/* <img className="vector" alt="Vector" src={vector} /> */}
          </div>

          <div className="text-wrapper-6">Livrés ce mois</div>

          <div className="text-wrapper-7">$1,146.45</div>

          <IconComponentNode className="more-vertical-outline" />
        </div>
      </div>
    </div>
  );
};