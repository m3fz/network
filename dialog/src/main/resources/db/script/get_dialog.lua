local dialogHashId = ARGV[1]

local dialogStr = redis.call('HGET', 'dialogs', dialogHashId)
if dialogStr then
  return dialogStr
else
  return "[]"
end
