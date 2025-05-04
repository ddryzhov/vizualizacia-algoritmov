import React from "react";
import { Typography, Paper } from "@mui/material";
import { useTranslation } from "react-i18next";

/**
 * Displays the description for the current analysis step.
 */
const StepDetails = React.memo(({ stepDetails }) => {
    const { t } = useTranslation();

    return (
        <>
            <Typography variant="h6" className="step-details-title">
                {t("Step Details")}
            </Typography>
            <Paper className="step-details">
                <Typography style={{ whiteSpace: "pre-line" }}>
                    {stepDetails || t("No details available")}
                </Typography>
            </Paper>
        </>
    );
});

export default StepDetails;
