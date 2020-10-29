const host = location.protocol + '//' + location.hostname + ':' + location.port
const apiBasePath = '/api/v1'
const basePath = host + apiBasePath

export const get = async (path) => {
  const result = await fetch(basePath + path)
  return result
}

export const del = async (path) => {
  return fetch(basePath + path, {
    method: 'DELETE'
  })
}

export const post = async (path, _body) => {
  return postPutPatch(path, 'POST', _body)
}

export const put = async (path, body) => {
  return postPutPatch(path, 'PUT', body)
}

export const patch = async (path, body) => {
  return postPutPatch(path, 'PATCH', body)
}

const postPutPatch = async (path, _method, _body) => {
  return fetch(basePath + path, {
    method: _method,
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(_body)
  })
}
