import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./login.css";

export const Login = () => {
  const navigate = useNavigate();

  // États pour stocker les valeurs des champs
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLoginClick = () => {
    console.log("Username:", username);
    console.log("Password:", password);

    // Redirection vers /home
    navigate("/TableauDeBord");
  };

  return (
    <div className="login">
      <div className="overlap-wrapper">
        <div className="overlap">
          <div className="overlap-group">
            <div className="text-wrapper">Welcome Back .!</div>

            <div className="frame">
              <div className="div">Skip the lag ?</div>
            </div>

            <div className="ellipse" />
            <div className="ellipse-2" />

            <div className="frame-wrapper">
              <div className="frame-2">
                <div className="frame-3">
                  <div className="upper-section">
                    <div className="login-text">
                      <div className="text-wrapper-2">Login</div>
                      <div className="text-wrapper-3">Glad you’re back.!</div>
                    </div>

                    <div className="credentials">
                      <div className="username">
                        <label className="text-wrapper-4" htmlFor="username">Username</label>
                        <input
                          id="username"
                          type="text"
                          className="input"
                          value={username}
                          onChange={(e) => setUsername(e.target.value)}
                        />
                      </div>

                      <div className="password-rem">
                        <div className="password">
                          <label className="text-wrapper-4" htmlFor="password">Password</label>
                          <input
                            id="password"
                            type="password"
                            className="input"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                          />
                        </div>

                        <div className="remember">
                          <input type="checkbox" id="remember" />
                          <label htmlFor="remember" className="text-wrapper-5">Remember me</label>
                        </div>
                      </div>

                      <div className="login-bt-fp">
                        <div className="div-wrapper" onClick={handleLoginClick}>
                          <div className="text-wrapper-6">Login</div>
                        </div>

                        <div className="text-wrapper-7">Forgot password ?</div>
                      </div>
                    </div>
                  </div>

                  <div className="other-logins">
                    <div className="or">
                      <div className="text-wrapper-8">Or</div>
                    </div>

                    <div className="frame-4">
                      {/* Boutons Google, Facebook, GitHub ici si tu veux les activer plus tard */}
                    </div>
                  </div>
                </div>

                <div className="frame-5">
                  <p className="don-t-have-an">
                    <span className="span">Don’t have an account ? </span>
                    <span className="text-wrapper-9">Signup</span>
                  </p>

                  <div className="customer-care">
                    <div className="frame-6"><div className="text-wrapper-10">Terms & Conditions</div></div>
                    <div className="frame-6"><div className="text-wrapper-10">Support</div></div>
                    <div className="frame-6"><div className="text-wrapper-10">Customer Care</div></div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Ligne décorative si besoin */}
        </div>
      </div>
    </div>
  );
};
