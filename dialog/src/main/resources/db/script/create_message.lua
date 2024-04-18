local dialogHashId = ARGV[1]
local payloadStr = ARGV[2]

local payload = cjson.decode(payloadStr)

local dialogStr = redis.call('HGET', 'dialogs', dialogHashId)
local dialog
if not dialogStr then
  dialog = {payload}
else
 dialog = cjson.decode(dialogStr)
 table.insert(dialog, payload)
end

local newDialogStr = cjson.encode(dialog)
redis.call('HSET', 'dialogs', dialogHashId, newDialogStr)
