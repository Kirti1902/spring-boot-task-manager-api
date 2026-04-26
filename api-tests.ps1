Write-Host "Creating Task..."
$response = Invoke-RestMethod `
 -Uri http://localhost:8080/tasks `
 -Method POST `
 -ContentType "application/json" `
 -Body '{ "title": "Script Task" }'

$taskId = $response.id
Write-Host "Created Task ID: $taskId"

Write-Host "`nGetting All Tasks..."
Invoke-RestMethod http://localhost:8080/tasks

Write-Host "`nGetting Task By ID..."
Invoke-RestMethod http://localhost:8080/tasks/$taskId

Write-Host "`nDeleting Task..."
Invoke-WebRequest `
 -Uri http://localhost:8080/tasks/$taskId `
 -Method DELETE

Write-Host "`nDone."
