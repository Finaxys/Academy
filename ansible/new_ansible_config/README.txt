# If something needs to be modified (ip adress, password ...)
# Password will be asked, ask to PO
ansible-vault edit secrets.txt


# To execute .yml file with encrypted variables
ansible-playbook config.yml --ask-vault-pass

