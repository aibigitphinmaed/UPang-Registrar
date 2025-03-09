<?php
include_once __DIR__ .'/../../includes/security/session_check.php';
requireLogin();
?>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.js"></script>
<script type="text/javascript" src="/js/chart-handler.js"></script>
<canvas id="staffDashboadChart" style="width:100%; max-width: 700px"></canvas>
